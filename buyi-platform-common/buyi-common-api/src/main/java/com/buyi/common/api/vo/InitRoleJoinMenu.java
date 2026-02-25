package com.buyi.common.api.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 */
@Data
public class InitRoleJoinMenu {

    private String tenantId;

    private List<InitRoleJoinMenuVo> initRoleJoinMenuList;

}
