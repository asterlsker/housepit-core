package com.asterlsker.housepit.core.annotation

import com.asterlsker.housepit.core.enums.AuthenticationScope
import java.lang.annotation.*
import java.lang.annotation.Retention
import java.lang.annotation.Target

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation class Authentication(
    val scope: AuthenticationScope = AuthenticationScope.REQUIRED
)