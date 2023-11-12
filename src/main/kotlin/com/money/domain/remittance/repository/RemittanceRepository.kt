package com.money.domain.remittance.repository

import com.money.domain.remittance.entity.*
import org.springframework.data.jpa.repository.JpaRepository

interface RemittanceRepository : JpaRepository<Remittance, Long> {
    fun findByFrom(from: Long): List<Remittance>
}