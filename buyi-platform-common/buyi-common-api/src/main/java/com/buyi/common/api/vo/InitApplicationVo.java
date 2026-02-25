package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 初始化应用
 */
@Data
public class InitApplicationVo {

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

    @Schema(description = "系统简称")
    private String sysSimpleName;

    @Schema(description = "显示顺序")
    private Integer showSort;

    @Schema(description = "是否显示0:不显示 1:显示")
    private String showFlag;

    @Schema(description = "系统呈现方式0:pc 1:微信小程序 2:android 3:IOS")
    private String showType;

    @Schema(description = "是否为灰色模式0:不是 1:是")
    private String isGray;

    @Schema(description = "是否可删除0：可删除  1：不可删除")
    private String isDeletable;

    @Schema(description = "系统说明")
    private String description;

    @Schema(description = "系统默认地址")
    private String sysUrl;

    @Schema(description = "图标路径")
    private String iconUrl;

    @Schema(description = "租户id")
    private String tenantId;
}
