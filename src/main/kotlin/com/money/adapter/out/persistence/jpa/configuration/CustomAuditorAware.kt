package com.money.adapter.out.persistence.jpa.configuration

import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomAuditorAware : AuditorAware<String> {

    override fun getCurrentAuditor() = Optional.of("money-api")

}