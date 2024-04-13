package team.cofoundery.backend.domain.user.controller

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.cofoundery.backend.domain.user.controller.request.LoginRequest
import team.cofoundery.backend.infra.client.kakao.KakaoAuthClient
import team.cofoundery.backend.infra.client.kakao.KakaoAuthLoginResponse
import java.math.BigInteger
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec
import java.util.Base64


@RestController
@RequestMapping("/auth")
class LoginController(
    @Value("\${kakao.client.id}")
    private val kakaoClientId: String,
    @Value("\${kakao.client.secret}")
    private val kakaoClientSecret: String,
    private val kakaoAuthClient: KakaoAuthClient,
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): KakaoAuthLoginResponse {
        val kakaoAuthLoginRequest = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to kakaoClientId,
            "redirect_uri" to loginRequest.redirectUri,
            "code" to loginRequest.code,
            "client_secret" to kakaoClientSecret,
        )
        val kakaoAuthLoginResponse = kakaoAuthClient.login(kakaoAuthLoginRequest)

        val claims = decodeClaims(kakaoAuthLoginResponse.id_token!!)

        val kakaoUserId = claims["sub"] as String
        val nickname = claims["nickname"] as String?

        return kakaoAuthLoginResponse
    }

    fun decodeClaims(idToken: String): Claims {
        val jwtParser = Jwts.parser()
            .requireIssuer("https://kauth.kakao.com")
            .requireAudience(kakaoClientId)
// TODO 페이로드의 exp 값이 현재 UNIX 타임스탬프(Timestamp)보다 큰 값인지 확인(ID 토큰이 만료되지 않았는지 확인)
// TODO 페이로드의 nonce 값이 카카오 로그인 요청 시 전달한 값과 일치하는지 확인
            .keyLocator { header ->
                val kid = header["kid"] as String

                val jwk = secrets[kid] ?: throw IllegalArgumentException("Unknown kid: $kid")

                val keySpec = RSAPublicKeySpec(
                    BigInteger(1, Base64.getUrlDecoder().decode(jwk.modulus)),
                    BigInteger(1, Base64.getUrlDecoder().decode(jwk.exponent)),
                )

                val keyFactory = KeyFactory.getInstance(jwk.keyType)

                keyFactory.generatePublic(keySpec)
            }
            .build()

        return jwtParser.parseSignedClaims(idToken).payload
    }

    // TODO retrieve from Kakao API
    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#oidc-find-public-key
    // https://kauth.kakao.com/.well-known/jwks.json
    private val secrets: Map<String, KakaoPublicJwk> = mapOf(
        "3f96980381e451efad0d2ddd30e3d3" to KakaoPublicJwk(
            keyType = "RSA",
            modulus = "q8zZ0b_MNaLd6Ny8wd4cjFomilLfFIZcmhNSc1ttx_oQdJJZt5CDHB8WWwPGBUDUyY8AmfglS9Y1qA0_fxxs-ZUWdt45jSbUxghKNYgEwSutfM5sROh3srm5TiLW4YfOvKytGW1r9TQEdLe98ork8-rNRYPybRI3SKoqpci1m1QOcvUg4xEYRvbZIWku24DNMSeheytKUz6Ni4kKOVkzfGN11rUj1IrlRR-LNA9V9ZYmeoywy3k066rD5TaZHor5bM5gIzt1B4FmUuFITpXKGQZS5Hn_Ck8Bgc8kLWGAU8TzmOzLeROosqKE0eZJ4ESLMImTb2XSEZuN1wFyL0VtJw",
            exponent = "AQAB",
        ),
        "9f252dadd5f233f93d2fa528d12fea" to KakaoPublicJwk(
            keyType = "RSA",
            modulus = "qGWf6RVzV2pM8YqJ6by5exoixIlTvdXDfYj2v7E6xkoYmesAjp_1IYL7rzhpUYqIkWX0P4wOwAsg-Ud8PcMHggfwUNPOcqgSk1hAIHr63zSlG8xatQb17q9LrWny2HWkUVEU30PxxHsLcuzmfhbRx8kOrNfJEirIuqSyWF_OBHeEgBgYjydd_c8vPo7IiH-pijZn4ZouPsEg7wtdIX3-0ZcXXDbFkaDaqClfqmVCLNBhg3DKYDQOoyWXrpFKUXUFuk2FTCqWaQJ0GniO4p_ppkYIf4zhlwUYfXZEhm8cBo6H2EgukntDbTgnoha8kNunTPekxWTDhE5wGAt6YpT4Yw",
            exponent = "AQAB",
        ),
    )
}
