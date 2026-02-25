package com.buyi.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @description 数据权限注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    String deptAlias() default "";
}

