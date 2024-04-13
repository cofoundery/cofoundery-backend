package team.cofoundery.backend

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

        return kakaoAuthLoginResponse
    }
}
