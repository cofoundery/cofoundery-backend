package team.cofoundery.backend.domain.user.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import team.cofoundery.backend.infra.client.kakao.KakaoAuthClient
import team.cofoundery.backend.infra.client.kakao.KakaoAuthLoginResponse

@Service
class KakaoAuthService(
    @Value("\${kakao.client.id}")
    private val kakaoClientId: String,
    @Value("\${kakao.client.secret}")
    private val kakaoClientSecret: String,
    private val kakaoAuthClient: KakaoAuthClient,
) {
    fun login(
        redirectUri: String,
        code: String,
    ): KakaoAuthLoginResponse {
        val kakaoAuthLoginRequest = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to kakaoClientId,
            "redirect_uri" to redirectUri,
            "code" to code,
            "client_secret" to kakaoClientSecret,
        )
        val kakaoAuthLoginResponse = kakaoAuthClient.login(kakaoAuthLoginRequest)
        return kakaoAuthLoginResponse
    }
}
