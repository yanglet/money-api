package com.money.application.port.out

import com.money.application.domain.model.Remittance

interface LoadRemittancesPort {
    fun readRemittances(memberNo: Long): List<Remittance>
}