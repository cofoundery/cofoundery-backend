package team.cofoundery.backend.common.dto

data class KakaoPublicJwk(
    val keyType: String,
    val modulus: String,
    val exponent: String,
)
