package com.buyi.common.api.openfign.admin;

import com.buyi.common.api.vo.InitOrgVo;
import com.buyi.common.api.vo.InitUserJoinOrgVo;
import com.buyi.common.core.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "buyi-system")
public interface IOrgInfoServiceFeign {

    /**
     * 初始化租户管理员
     * @param initOrgVo 初始化机构信息
     * @return Result<?>
     */
    @PostMapping("/sys-org-info/initTenantOrg")
    CommonResult<InitOrgVo> initTenantOrg(@RequestBody InitOrgVo initOrgVo);

    /**
     * 初始化用户与机构的关联关系
     * @param initUserJoinOrg 实体类
     * @return Result<?>
     */
    @PostMapping("/sys-user-join-org/initUserJoinOrg")
    CommonResult<InitOrgVo> initUserJoinOrg(@RequestBody InitUserJoinOrgVo initUserJoinOrg);
}
