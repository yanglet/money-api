package com.money.application.port.`in`.dto

import com.money.application.domain.model.Money

data class DepositCommand(
    val amount: Money
) {

    init {
        require(amount.isPositiveOrZero())
    }

}