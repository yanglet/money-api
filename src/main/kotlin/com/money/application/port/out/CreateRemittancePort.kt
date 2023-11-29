package com.money.application.port.out

import com.money.application.domain.model.Remittance

interface CreateRemittancePort {
    fun createRemittance(remittance: Remittance)
}