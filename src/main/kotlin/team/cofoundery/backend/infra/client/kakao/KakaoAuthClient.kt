package team.cofoundery.backend.infra.client.kakao

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


/**
 * @see[https://developers.kakao.com/docs/latest/ko/kakaologin/common]
 */
@FeignClient(name = "kakao", url = "https://kauth.kakao.com")
interface KakaoAuthClient {
    /**
     * @see[https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token]
     */
    @PostMapping(
        "/oauth/token",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
    )
    fun login(@RequestBody kakaoAuthLoginRequest: Map<String, *>): KakaoAuthLoginResponse

    @PostMapping(
        "/oauth/token",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
    )
    // TODO this doesn't work, even though using SpringFormEncoder
    fun login(@RequestBody kakaoAuthLoginRequest: KakaoAuthLoginRequest): KakaoAuthLoginResponse

    /**
     * @see[https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#oidc-find-public-key]
     */
    @GetMapping(
        "/.well-known/jwks.json",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun listPublicJwks(): KakaoAuthListPublicJwksResponse
}
