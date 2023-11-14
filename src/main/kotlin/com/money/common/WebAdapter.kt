package com.money.common

import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RestController


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RestController
annotation class WebAdapter(
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any (or empty String otherwise)
     */
    @get:AliasFor(annotation = RestController::class) val value: String = ""
)

