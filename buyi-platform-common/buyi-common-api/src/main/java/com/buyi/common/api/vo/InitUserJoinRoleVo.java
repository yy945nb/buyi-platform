package com.buyi.common.api.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description:
 * @author: fgw
 * @createDate: 2024/3/13
 */
@Data
@AllArgsConstructor
public class InitUserJoinRoleVo {

    @Schema(description = "角色id")
    @TableId(value = "role_id")
    private String roleId;

    @Schema(description = "用户id")
    @TableField(value = "user_id", insertStrategy = FieldStrategy.ALWAYS)
    private String userId;

    @Schema(description = "租户id")
    @TableField("tenant_id")
    private String tenantId;
}
