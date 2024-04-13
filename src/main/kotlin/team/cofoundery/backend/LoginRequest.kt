package team.cofoundery.backend

data class LoginRequest(
    val redirectUri: String,
    val code: String,
)
