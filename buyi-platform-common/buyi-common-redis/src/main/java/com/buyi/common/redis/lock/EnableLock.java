package com.buyi.common.redis.lock;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @summary 开启分布式锁注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//@Import(LockManager.class)
public @interface EnableLock {

}
