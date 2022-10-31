package com.asterlsker.housepit.core.annotation

import com.asterlsker.housepit.core.enums.AuthenticationScope
import kotlin.annotation.Retention
import kotlin.annotation.Target

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authentication(
    val scope: AuthenticationScope = AuthenticationScope.REQUIRED
)