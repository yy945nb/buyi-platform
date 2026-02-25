package com.buyi.common.core.enums;

import lombok.Getter;

/**
 * @description: 租户数据隔离类型
 */
public enum TenantDataSourceTypeEnum implements IBaseEnum<String>{

    /**
     * 基于数据库隔离
     */
    DATABASE("database", "基于数据库隔离"),
    /**
     * 基于数据表隔离
     */
    TABLE("table", "基于数据表隔离"),

    /**
     * 基于字段隔离
     */
    FIELD("filed", "基于字段隔离");

    @Getter
    private String value;

    @Getter
    private String label;

    TenantDataSourceTypeEnum(String type, String label) {
        this.value = type;
        this.label = label;
    }
}
