package com.example.remittance.out.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface RemittanceJpaRepository : JpaRepository<RemittanceEntity, Long> {
}