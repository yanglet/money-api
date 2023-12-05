package com.money.application.domain.exception

import com.money.common.exception.BusinessException

class BalanceInsufficientException(message: String) : BusinessException(message) {
}