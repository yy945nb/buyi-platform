package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @description: 初始化角色
 */
@Data
public class InitRoleVo {
    @Schema(description = "id")
    private String id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "租户id")
    private String tenantId;


    @Schema(description = "英文名称")
    private String enname;

    @Schema(description = "角色类型0:系统角色 1:用户添加角色")
    private String roleType;

    @Schema(description = "所属机构id")
    private String officeId;

    @Schema(description = "数据权限保存角色对应的数据权限")
    private String dataScope;

    @Schema(description = "是否用于注册")
    private String usedRegister;

    @Schema(description = "排序正序")
    private Integer showSort;

    @Schema(description = "是否可删除")
    private String isDeletable;


    @Schema(description = "管理员用户Id")
    private String adminUserId;


    @Schema(description = "初始化应用信息")
    private List<InitApplicationVo> initApplicationVoList;

    @Schema(description = "初始化菜单信息")
    private List<InitMenuVo> initRoleJoinMenuList;

    public InitRoleVo buildDefaultMenu(String tenantId,String officeId) {
        this.name = "默认管理员";
        this.tenantId = tenantId;
        this.enname = "admin";
        this.dataScope = "1";
        this.roleType = "0";
        this.officeId = officeId;
        this.usedRegister = "0";
        this.showSort = 1;
        this.isDeletable = "0";
        return this;
    }
}
