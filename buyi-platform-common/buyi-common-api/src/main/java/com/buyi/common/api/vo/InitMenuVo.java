package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 初始化菜单
 */
@Data
public class InitMenuVo {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "父级编号")
    private String parentId;

    @Schema(description = "父级编号列表")
    private String parentIds;

    @Schema(description = "父级名称列表")
    private String parentNames;

    @Schema(description = "是否未叶子节点，0否1是")
    private String treeLeaf;

    @Schema(description = "树层级")
    private Integer treeLevel;

    @Schema(description = "租户id")
    private String tenantId;


    @Schema(description = "应用id")
    private String applicationId;

    @Schema(description = "菜单类型0:菜单 1:按钮 2:组件")
    private String type;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "链接")
    private String herf;

    @Schema(description = "目标")
    private String target;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "是否固定tagsview")
    private String isAffix;

    @Schema(description = "是否为主题展示姐main")
    private String isScreen;

    @Schema(description = "是否keep-alive")
    private String isKeepAlive;

    @Schema(description = "是否在菜单展示")
    private String isShow;

    @Schema(description = "权限标识")
    private String permisson;

    @Schema(description = "是否可删除")
    private String isDeletable;

    @Schema(description = "系统说明")
    private String description;

    @Schema(description = "系统默认地址")
    private String sysUrl;

    @Schema(description = "图标路径")
    private String iconUrl;


}
