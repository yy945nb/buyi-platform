package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: fgw
 * @createDate: 2024/3/13
 */
@Data
public class InitRoleJoinMenuVo {
    @Schema(description = "角色id")
    private String roleId;

    @Schema(description = "菜单id")
    private String menuId;

    @Schema(description = "租户id")
    private String tenantId;
}
