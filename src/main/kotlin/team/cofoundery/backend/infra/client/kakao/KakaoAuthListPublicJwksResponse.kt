package team.cofoundery.backend.infra.client.kakao

data class KakaoAuthListPublicJwksResponse(
    val keys: List<KakaoAuthPublicJwkResponse>,
)
