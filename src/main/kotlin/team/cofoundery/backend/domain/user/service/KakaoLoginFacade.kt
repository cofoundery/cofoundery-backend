package team.cofoundery.backend.domain.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.cofoundery.backend.common.dto.UserDto

@Service
class KakaoLoginFacade(
    private val kakaoAuthService: KakaoAuthService,
    private val kakaoOpenIdConnectService: KakaoOpenIdConnectService,
    private val userService: UserService,
) {
    @Transactional
    fun login(
        redirectUri: String,
        code: String,
    ): UserDto {
        val kakaoAuthLoginResponse = kakaoAuthService.login(
            redirectUri = redirectUri,
            code = code,
        )

        val (kakaoUserId, nickname) = kakaoOpenIdConnectService.decodeClaims(kakaoAuthLoginResponse.id_token!!)

        val user = userService.getUserByKakaoUserId(kakaoUserId)
            ?: userService.createUser(kakaoUserId, nickname)

        return user
    }
}
