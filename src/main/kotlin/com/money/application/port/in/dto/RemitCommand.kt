package com.money.application.port.`in`.dto

import com.money.application.domain.model.Money

data class RemitCommand(
    val to: Long,
    val from: Long,
    val money: Money
) {

    init {
        require(to > 0)
        require(from > 0)
        require(money.isPositive())
    }

}
