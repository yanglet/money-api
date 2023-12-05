package com.money.application.port.`in`.dto

import com.money.application.domain.model.Money

data class DepositCommand(
    val money: Money
) {

    init {
        require(money.isPositiveOrZero())
    }

}