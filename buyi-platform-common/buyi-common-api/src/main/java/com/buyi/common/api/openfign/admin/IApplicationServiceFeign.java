package com.buyi.common.api.openfign.admin;

import com.buyi.common.api.vo.InitRoleJoinApplication;
import com.buyi.common.api.vo.InitTenantApplication;
import com.buyi.common.core.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: 应用管理
 */
@FeignClient(name = "buyi-system")
public interface IApplicationServiceFeign {

    /**
     * 初始化租户应用信息
     *
     * @param initTenantApplication 初始化租户应用信息
     * @return 初始化租户应用信息
     */
    @PostMapping("/sys-application/initTenantApplication")
    CommonResult<?> initTenantApplication(@RequestBody InitTenantApplication initTenantApplication);


    @PostMapping("/sys-role-join-application/initRoleJoinApplication")
    CommonResult<?> initRoleJoinApplication(@RequestBody InitRoleJoinApplication initRoleJoinApplication);
}
