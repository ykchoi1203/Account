package com.example.account.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // 어노테이션을 붙일 수 있는 타입
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited  // 상속가능한 구조로 사용하기 위한 어노테이션
public @interface AccountLock {
    long tryLockTime() default 5000L;   // 해당 시간동안 기다리기.
}
