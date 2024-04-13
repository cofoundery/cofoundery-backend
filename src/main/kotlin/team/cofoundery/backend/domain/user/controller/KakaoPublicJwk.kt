package team.cofoundery.backend.domain.user.controller

data class KakaoPublicJwk(
    val keyType: String,
    val modulus: String,
    val exponent: String,
)
