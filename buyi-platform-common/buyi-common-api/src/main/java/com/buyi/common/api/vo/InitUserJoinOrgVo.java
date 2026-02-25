package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 初始化用户关联机构
 * @author: fgw
 * @createDate: 2024/3/13
 */
@Data
public class InitUserJoinOrgVo {

    @Schema(description = "用户编号")
    private String userId;

    @Schema(description = "机构编号")
    private String orgId;

    @Schema(description = "1-公司，2-部门 3-小组")
    private String type;

    @Schema(description = "是否是主部门 0:不是 1: 是")
    private String isMain;

    @Schema(description = "租户id")
    private String tenantId;
}
