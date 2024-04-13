package team.cofoundery.backend.domain.user.service

import io.jsonwebtoken.Jwts
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import team.cofoundery.backend.dto.KakaoOpenIdPayload
import team.cofoundery.backend.dto.KakaoPublicJwk
import team.cofoundery.backend.infra.client.kakao.KakaoAuthClient
import java.math.BigInteger
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec
import java.util.Base64
import java.util.concurrent.TimeUnit

@Service
class KakaoOpenIdConnectService(
    @Value("\${kakao.client.id}")
    private val kakaoClientId: String,
    private val kakaoAuthClient: KakaoAuthClient,
) {
    private lateinit var secrets: Map<String, KakaoPublicJwk>

    @PostConstruct
    @Scheduled(
        initialDelay = 30,
        fixedRate = 30,
        timeUnit = TimeUnit.SECONDS
    )
    fun init() {
        println("Init Kakao secrets")
        secrets = kakaoAuthClient
            .listPublicJwks()
            .keys
            .associateBy { it.kid }
            .mapValues { (_, jwk) ->
                KakaoPublicJwk(
                    keyType = jwk.kty,
                    modulus = jwk.n,
                    exponent = jwk.e,
                )
            }
    }

    fun decodeClaims(idToken: String): KakaoOpenIdPayload {
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

        val claims = jwtParser.parseSignedClaims(idToken).payload

        return KakaoOpenIdPayload(
            kakaoUserId = claims["sub"] as String,
            nickname = claims["nickname"] as String?,
        )
    }
}
