package team.cofoundery.backend.domain.user.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.cofoundery.backend.domain.user.controller.request.LoginRequest
import team.cofoundery.backend.domain.user.controller.response.UserResponse
import team.cofoundery.backend.domain.user.service.KakaoLoginFacade


@RestController
@RequestMapping("/auth")
class LoginController(
    private val kakaoLoginFacade: KakaoLoginFacade,
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): UserResponse {
        val userDto = kakaoLoginFacade.login(
            redirectUri = loginRequest.redirectUri,
            code = loginRequest.code,
        )
        val userResponse = UserResponse(
            id = userDto.id,
            kakaoUserId = userDto.kakaoUserId,
            nickname = userDto.nickname,
        )
        return userResponse
    }
}
