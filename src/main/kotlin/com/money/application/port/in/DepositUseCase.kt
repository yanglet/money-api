package com.money.application.port.`in`

import com.money.application.port.`in`.dto.DepositCommand

interface DepositUseCase {
    fun deposit(memberNo: Long, command: DepositCommand)
}