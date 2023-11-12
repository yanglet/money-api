package com.money.global.advice

import com.fasterxml.jackson.core.*
import com.money.global.*
import com.money.global.advice.dto.*
import com.money.global.exception.*
import org.springframework.http.*
import org.springframework.http.HttpStatus.*
import org.springframework.http.converter.*
import org.springframework.web.bind.annotation.*

@RestControllerAdvice
class GlobalExceptionHandler : Log {
    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ExceptionResponse> {
        log.error("BusinessException", e)
        return ResponseEntity.status(BAD_REQUEST).body(
            ExceptionResponse(e.message)
        )
    }

    @ExceptionHandler(JsonParseException::class)
    protected fun handleJsonParseException(e: JsonParseException): ResponseEntity<ExceptionResponse> {
        log.error("JsonParseException", e)
        return ResponseEntity.status(BAD_REQUEST).body(
            ExceptionResponse(e.message)
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ExceptionResponse> {
        log.error("HttpMessageNotReadableException", e)
        return ResponseEntity.status(BAD_REQUEST).body(
            ExceptionResponse(e.message)
        )
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ExceptionResponse> {
        log.error("Exception", e)
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
            ExceptionResponse(e.message)
        )
    }
}