package com.money.application.port.out.dto

import com.money.application.domain.model.Money
import com.money.application.domain.model.Remittance
import com.money.application.domain.model.RemittanceStatus

data class CreateRemittanceCommand(
    val to: Long,
    val from: Long,
    val money: Money,
    val status: RemittanceStatus,
    val reason: String?
) {

    companion object {
        fun from(remittance: Remittance) = CreateRemittanceCommand(
            to = remittance.getTo().getMemberNo(),
            from = remittance.getFrom().getMemberNo(),
            money = remittance.getMoney(),
            status = remittance.getStatus(),
            reason = remittance.getReason()
        )
    }
}