package com.buyi.common.api.vo;

import com.buyi.common.core.model.vo.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 */
@Data
public class InitOrgVo extends BaseVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "租户id")
    private String tenantId;


    @Schema(description = "简称")
    private String simpleName;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "其他表不能使用此字段进行关联，存在重复的情况")
    private String code;

    @Schema(description = "1-公司，2-部门 3-小组")
    private String type;

    @Schema(description = "0-自身，1-服务方，如果有需求做区分，可使用此字段")
    private String orgType;


    @Schema(description = "负责人")
    private String master;

    @Schema(description = "负责人姓名")
    private String masterName;

    @Schema(description = "机构地址")
    private String address;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "传真")
    private String fax;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "0-未启用，1-启用")
    private String useable;

    @Schema(description = "是否可删除0：可删除  1：不可删除")
    private String isDeletable;

    public InitOrgVo  buildDefault(String tenantId,String name,String phone,String master,String masterName) {
        this.tenantId = tenantId;
        this.name = name;
        this.simpleName = name;
        this.phone = phone;
        this.master = master;
        this.masterName = masterName;
        this.orgType = "0";
        this.useable = "1";
        this.isDeletable = "0";
        return this;
    }
}
