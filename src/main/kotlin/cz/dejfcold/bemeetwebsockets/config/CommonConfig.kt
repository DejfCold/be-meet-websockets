package cz.dejfcold.bemeetwebsockets.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class CommonConfig {

    @Primary
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule()
}