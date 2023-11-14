package com.money.common.exception

class DataNotFoundException(override val message: String?) : RuntimeException(message) {
}