package com.example.remittance.out

import com.example.remittance.out.jpa.RemittanceJpaRepository
import com.example.remittance.port.out.RemitOutPort

class RemittanceAdapter(
    private val remittanceJpaRepository: RemittanceJpaRepository
) : RemitOutPort {
    override fun remit() {
        println("=================================== RemittanceAdapter.remit()")
    }
}