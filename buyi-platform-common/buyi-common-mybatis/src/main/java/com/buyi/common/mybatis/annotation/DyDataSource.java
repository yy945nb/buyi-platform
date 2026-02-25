package com.buyi.common.mybatis.annotation;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;

import java.lang.annotation.*;

/**
 * @description: 数据源切换注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@DSTransactional
public @interface DyDataSource {

    String dataSourceName() default "";

    // 自行指定租户编号，不从请求头获取，不指定则从请求都获取
    String tenantId() default "";

    // 为true 不进行数据源切换，使用场景为系统管理员租户的用户对系统租户进行操作时使用
    boolean self() default false;

    // 是否自动设置tenantId
    // false 为忽略多租户插件
    boolean autoTenantId() default true;
}
