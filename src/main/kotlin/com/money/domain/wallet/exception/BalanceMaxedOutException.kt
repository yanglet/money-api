package com.money.domain.wallet.exception

import com.money.global.exception.*

class BalanceMaxedOutException(message: String) : BusinessException(message) {
}