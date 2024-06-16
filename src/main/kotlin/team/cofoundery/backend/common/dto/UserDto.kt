package team.cofoundery.backend.common.dto

import team.cofoundery.backend.domain.user.entity.User

data class UserDto(
    val id: Long,
    val kakaoUserId: String?,
    val nickname: String?,
) {
    constructor(user: User) : this(
        id = user.id!!,
        kakaoUserId = user.kakaoUserId,
        nickname = user.nickname,
    )
}
