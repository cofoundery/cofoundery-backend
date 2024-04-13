package team.cofoundery.backend

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(name = "kakao", url = "https://kauth.kakao.com")
interface KakaoAuthClient {
    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
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
}
