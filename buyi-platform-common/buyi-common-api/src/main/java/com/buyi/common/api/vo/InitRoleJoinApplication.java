package com.buyi.common.api.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: fgw
 * @createDate: 2024/3/13
 */
@Data
public class InitRoleJoinApplication {

    private String tenantId;

    private List<InitRoleJoinApplicationVo> initRoleJoinApplicationList;
}
