package com.buyi.common.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.buyi.common.mybatis.util.TenantHelper;
import com.buyi.common.oauth.util.AccessTokenUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;

import java.util.List;

/**
 * @description: 多租户插件
 */
public class CustomizeTenantLineHandler implements TenantLineHandler {
    @Override
    public Expression getTenantId() {
        //  从上下文中获取租户id
        String tenantId = StrUtil.isNotEmpty(AccessTokenUtils.getTenantId()) ? AccessTokenUtils.getTenantId() : "0";
        return new StringValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return TenantLineHandler.super.getTenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        Boolean isAuto = TenantHelper.get();
        if (isAuto != null) {
            return tableName.startsWith("g_tbl_") || tableName.startsWith("oauth2_") || "undo_log".equals(tableName) || (!isAuto);
        }
        return tableName.startsWith("g_tbl_") || tableName.startsWith("oauth2_") || "undo_log".equals(tableName);
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
