package com.money.global.configuration

import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.models.*
import io.swagger.v3.oas.models.info.*
import org.springframework.context.annotation.*

@Configuration
@OpenAPIDefinition
class SwaggerConfiguration {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(Components())
        .info(
            Info()
                .title("Money API")
                .description("money api docs")
                .version("1.0.0")
        )
}