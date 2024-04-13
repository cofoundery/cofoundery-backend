package team.cofoundery.backend.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.cofoundery.backend.domain.user.entity.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByKakaoUserId(kakaoUserId: String): User?
}
