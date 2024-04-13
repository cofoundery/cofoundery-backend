package team.cofoundery.backend

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import feign.form.FormProperty


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoAuthLoginRequest(
    // "authorization_code" or "refresh_token"
    @FormProperty("grant_type")
    val grantType: String,
    @FormProperty("client_id")
    val clientId: String,
    @FormProperty("redirect_uri")
    val redirectUri: String,
    @FormProperty("code")
    val code: String,
    @FormProperty("client_secret")
    val clientSecret: String,
)
