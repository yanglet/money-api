package com.money.domain.wallet.exception

import com.money.global.exception.*

class BalanceInsufficientException(message: String) : BusinessException(message) {
}