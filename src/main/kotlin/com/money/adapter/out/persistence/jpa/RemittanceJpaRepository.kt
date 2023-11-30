package com.money.adapter.out.persistence.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface RemittanceJpaRepository : JpaRepository<RemittanceJpaEntity, Long> {
    fun findByFrom(from: Long): List<RemittanceJpaEntity>
}