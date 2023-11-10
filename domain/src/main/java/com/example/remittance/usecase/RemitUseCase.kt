package com.example.remittance.usecase

import com.example.remittance.port.`in`.RemitInPort
import com.example.remittance.port.out.RemitOutPort
import org.springframework.stereotype.Service

@Service
class RemitUseCase(
    private val remitOutPort: RemitOutPort
) : RemitInPort {
    override fun remit() {
        println("=================================== RemitUseCase.remit()")
        remitOutPort.remit()
    }
}