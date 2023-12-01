package com.money.adapter.out.persistence.jpa.common

import jakarta.persistence.*
import org.springframework.data.annotation.*
import org.springframework.data.jpa.domain.support.*
import java.time.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "insert_date", updatable = false)
    lateinit var insertDate: LocalDateTime
        protected set

    @CreatedBy
    @Column(name = "insert_operator", updatable = false)
    lateinit var insertOperator: String
        protected set

    @LastModifiedDate
    @Column(name = "update_date")
    lateinit var updateDate: LocalDateTime
        protected set

    @LastModifiedBy
    @Column(name = "update_operator")
    lateinit var updateOperator: String
        protected set

}