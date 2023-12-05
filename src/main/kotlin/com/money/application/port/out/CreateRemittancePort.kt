package com.money.application.port.out

import com.money.application.port.out.dto.CreateRemittanceCommand

interface CreateRemittancePort {
    fun createRemittance(command: CreateRemittanceCommand)
}