package com.buyi.common.api.openfign.admin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.buyi.common.api.vo.InitRoleVo;
import com.buyi.common.core.result.CommonResult;

/**
 * @description:
 */
@FeignClient(name = "buyi-system")
public interface IRoleServiceFeign {

    /**
     * 初始化租户角色信息
     * @param initRoleVo 初始化租户角色信息
     * @return 初始化租户角色信息
     */
    @PostMapping("/sys-role/initTenantRole")
    CommonResult<InitRoleVo> initTenantRole(@RequestBody InitRoleVo initRoleVo);
}
