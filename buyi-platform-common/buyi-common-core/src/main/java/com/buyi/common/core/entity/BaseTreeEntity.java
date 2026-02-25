package com.buyi.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description: 树形结构数据
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseTreeEntity<T extends BaseTreeEntity> extends BaseEntity {

    @Schema(description = "节点名称")
    @TableField("name")
    private String name;

    @Schema(description = "父级编号")
    @TableField("parent_id")
    private String parentId;

    @Schema(description = "父级编号列表")
    @TableField("parent_ids")
    private String parentIds;

    @Schema(description = "父级名称列表")
    @TableField("parent_names")
    private String parentNames;

    @Schema(description = "是否未叶子节点，0否1是")
    @TableField("tree_leaf")
    private String treeLeaf;

    @Schema(description = "树层级")
    @TableField("tree_level")
    private Integer treeLevel;

    @Schema(description = "子节点")
    @TableField(exist = false)
    private List<T> children;
}
