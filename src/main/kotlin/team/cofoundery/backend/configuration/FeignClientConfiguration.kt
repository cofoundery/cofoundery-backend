package team.cofoundery.backend.configuration

import feign.Logger
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.cofoundery.backend.BASE_PACKAGE

@Configuration
@EnableFeignClients(
    basePackages = [BASE_PACKAGE]
)
class FeignClientConfiguration {
    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }
}
