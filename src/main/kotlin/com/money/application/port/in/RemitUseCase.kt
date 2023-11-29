package com.money.application.port.`in`

import com.money.application.port.`in`.dto.RemitCommand

interface RemitUseCase {
    fun remit(command: RemitCommand)
}