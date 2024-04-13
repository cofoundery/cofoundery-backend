package team.cofoundery.backend.domain.user.controller.request

data class LoginRequest(
    val redirectUri: String,
    val code: String,
)
