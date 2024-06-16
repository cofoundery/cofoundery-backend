package team.cofoundery.backend.domain.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.cofoundery.backend.common.dto.UserDto
import team.cofoundery.backend.domain.user.entity.User
import team.cofoundery.backend.domain.user.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun getUserByKakaoUserId(kakaoUserId: String): UserDto? {
        val user = userRepository.findByKakaoUserId(kakaoUserId)
            ?: return null

        return UserDto(user)
    }

    @Transactional
    fun createUser(kakaoUserId: String, nickname: String?): UserDto {
        val user = User(
            kakaoUserId = kakaoUserId,
            nickname = nickname,
        )
        userRepository.save(user)
        return UserDto(user)
    }
}
