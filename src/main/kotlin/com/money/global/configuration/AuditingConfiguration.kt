package com.money.global.configuration

import org.springframework.context.annotation.*
import org.springframework.data.jpa.repository.config.*

@Configuration
@EnableJpaAuditing
class AuditingConfiguration {
}