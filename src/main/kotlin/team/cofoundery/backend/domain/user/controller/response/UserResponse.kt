package team.cofoundery.backend.domain.user.controller.response

data class UserResponse(
    val id: Int,
    val kakaoUserId: String,
    val nickname: String?,
)
