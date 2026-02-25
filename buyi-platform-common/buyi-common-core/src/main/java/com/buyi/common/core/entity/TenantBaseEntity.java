package com.buyi.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @description: 租户下通用实体类
 */
@Getter
@Setter
@Accessors(chain = true)
public class TenantBaseEntity extends BaseEntity {

    @Schema(description = "租户id")
    @TableField("tenant_id")
    private String tenantId;

}
