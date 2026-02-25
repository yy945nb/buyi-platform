package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: fgw
 * @createDate: 2024/3/13
 */
@Data
public class InitRoleJoinApplicationVo {


    @Schema(description = "角色id")
    private String roleId;

    @Schema(description = "应用id")
    private String applicationId;

    @Schema(description = "租户id")
    private String tenantId;
}
