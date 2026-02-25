package com.buyi.common.redis.lock;

import java.lang.annotation.*;

/**
 * 方法分布式锁
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock{

    /**
     * 锁持续时间 单位秒
     * 默认1分钟
     * @return
     */
    int lockTime() default 60;

    /**
     * redis锁的key前缀
     * 如果为空，则默认为类名+方法名
     * @return
     */
    String keyPrex() default "";

    /**
     * 请求锁的超时时间(单位：毫秒)
     * @return
     */
    long timetOut() default 300L;

    /**
     * 获取锁失败时候的失败提示
     * @return
     */
    String errorMessage() default "";
}
