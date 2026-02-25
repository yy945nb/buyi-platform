package com.buyi.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @description: 租户树形结构数据
 */
@Getter
@Setter
@Accessors(chain = true)
public class TenantBaseTreeEntity extends BaseTreeEntity<TenantBaseTreeEntity>{

    @Schema(description = "租户id")
    @TableField("tenant_id")
    private String tenantId;
}
