package com.buyi.common.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.buyi.common.oauth.util.AccessTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @description: 字段自动填充处理器
 */
@Slf4j
@Component
public class CustomizeMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime::now, LocalDateTime.class);
        if (StrUtil.isNotBlank(AccessTokenUtils.getUserId())) {
            this.strictInsertFill(metaObject, "createdBy", AccessTokenUtils::getUserId, String.class);
            this.strictInsertFill(metaObject, "updatedBy", AccessTokenUtils::getUserId, String.class);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime::now, LocalDateTime.class);
        if (StrUtil.isNotBlank(AccessTokenUtils.getUserId())) {
            this.strictUpdateFill(metaObject, "updatedBy", AccessTokenUtils::getUserId, String.class);
        }
    }
}
