package org.bipmed.brave.import.rest

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestConfiguration {

    @Value("\${url}")
    val url: String = ""

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.rootUri(url).build()
    }
}