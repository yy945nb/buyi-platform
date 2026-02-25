package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description:
 * @author: fgw
 * @createDate: 2024/3/6
 */
@Data
@Accessors(chain = true)
public class InitTenantMenu {

    @Schema(description = "租户id")
    private String tenantId;

    List<InitMenuVo> initMenuVoList;
}
