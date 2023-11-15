package com.money.application.domain

import com.money.application.domain.model.Member
import com.money.application.domain.model.MemberStatus
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class MemberFreeSpec : FreeSpec({

    "isActive" - {
        val activeMember = Member.of(1, MemberStatus.ACTIVE)

        activeMember.isActive() shouldBe true
    }

    "isNonActive" - {
        val nonActiveMember = Member.of(1, MemberStatus.NON_ACTIVE)

        nonActiveMember.isNonActive() shouldBe true
    }

})