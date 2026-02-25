package com.buyi.common.mybatis.util;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 动态表名构建工具类
 */
public class TableNameBuildUtil {

    private static final String POINT = ".";
    private static final String DIAGONAL_QUOTATION = "`";

    /**
     * 根据租户信息构建表名
     *
     * @param tableName mybatis-plus返回的表名
     * @param tenantId  租户id
     * @param useDb     是否使用分库策略
     * @return 构建后的表名
     */
    public static String build(String tableName, String tenantId, Boolean useDb) {
        tenantId = StrUtil.isNotEmpty(tenantId) ? "_" + tenantId : "";
        if (tableName.indexOf(POINT) > 0 && useDb) {
            // 处理带库名的表名
            String dbName = tableName.substring(0, tableName.indexOf("."));
            if (dbName.startsWith(DIAGONAL_QUOTATION) && dbName.endsWith(DIAGONAL_QUOTATION)) {
                // 处理带`的库名
                dbName = dbName.substring(dbName.indexOf(DIAGONAL_QUOTATION) + 1, dbName.lastIndexOf(DIAGONAL_QUOTATION));
                dbName = DIAGONAL_QUOTATION + dbName + tenantId + DIAGONAL_QUOTATION;
            } else {
                dbName = dbName + tenantId;
            }
            String tableName2 = tableName.substring(tableName.indexOf(POINT) + 1) + tenantId;
            return dbName + POINT + tableName2;
        }
        return tableName + tenantId;
    }


    /**
     * 根据url获取库名
     *
     * @param url
     * @return
     */
    public static String getDataBaseName(String url) {
        Pattern p = Pattern.compile("jdbc:(?<db>\\w+):.*((//)|@)(?<host>.+):(?<port>\\d+)(/|(;DatabaseName=)|:)(?<dbName>\\w+.+)\\?");
        Matcher m = p.matcher(url);
        if (m.find()) {
            return m.group("dbName");
        }
        return null;
    }
}
