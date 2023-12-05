package com.money.application.domain.exception

import com.money.common.exception.BusinessException

class BalanceMaxedOutException(message: String) : BusinessException(message) {
}