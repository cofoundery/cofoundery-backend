package team.cofoundery.backend.infra.client.kakao

data class KakaoAuthLoginResponse(
    // TODO rename to camelCase
    val token_type: String,
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val refresh_token_expires_in: Int,
    val id_token: String?,
    val scope: String?,
)
