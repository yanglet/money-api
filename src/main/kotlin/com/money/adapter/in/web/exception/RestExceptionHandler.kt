package com.money.adapter.`in`.web.exception

import com.money.common.exception.BusinessException
import com.money.common.exception.DataNotFoundException
import com.money.common.log.Log
import org.springframework.boot.json.JsonParseException
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler : Log {

    /**
     * throwable 을 api 응답으로 던지는 것은 주의하자.
     */
    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ExceptionResponse> {
        log.error("BusinessException", e)
        return ResponseEntity.status(BAD_REQUEST).body(
            ExceptionResponse(e.message, e)
        )
    }

    @ExceptionHandler(DataNotFoundException::class)
    protected fun handleDataNotFoundException(e: DataNotFoundException): ResponseEntity<ExceptionResponse> {
        log.error("DataNotFoundException", e)
        return ResponseEntity.status(BAD_REQUEST).body(
            ExceptionResponse(e.message, e)
        )
    }

    @ExceptionHandler(JsonParseException::class)
    protected fun handleJsonParseException(e: JsonParseException): ResponseEntity<ExceptionResponse> {
        log.error("JsonParseException", e)
        return ResponseEntity.status(BAD_REQUEST).body(
            ExceptionResponse(e.message, e)
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    protected fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ExceptionResponse> {
        log.error("IllegalArgumentException", e) // validation exception
        return ResponseEntity.status(BAD_REQUEST).body(
            ExceptionResponse(e.message, e)
        )
    }

    @ExceptionHandler(Exception::class)
    protected fun handleJsonParseException(e: Exception): ResponseEntity<ExceptionResponse> {
        log.error("Exception", e)
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
            ExceptionResponse(e.message, e)
        )
    }

}