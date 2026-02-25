package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description:
 */
@Data
@Accessors(chain = true)
public class InitTenantApplication {
    @Schema(description = "租户id")
    private String tenantId;

    List<InitApplicationVo> applicationVoList;
}
