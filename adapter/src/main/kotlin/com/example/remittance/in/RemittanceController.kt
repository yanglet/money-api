package com.example.remittance.`in`

import com.example.remittance.port.`in`.RemitInPort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/remittances")
class RemittanceController(
    private val remitInPort: RemitInPort
) {
    @GetMapping
    fun remit() {
        remitInPort.remit()
    }
}