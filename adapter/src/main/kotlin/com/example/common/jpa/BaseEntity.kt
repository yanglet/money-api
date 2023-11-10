package com.example.common.jpa

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

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