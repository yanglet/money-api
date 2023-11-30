package com.money.application.port.`in`

import com.money.application.domain.model.Remittance

interface ReadRemittancesUseCase {
    fun readRemittances(memberNo: Long): List<Remittance>
}