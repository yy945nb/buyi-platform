package com.buyi.common.api.openfign.admin;

import com.buyi.common.core.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.buyi.common.api.vo.InitRoleJoinMenu;
import com.buyi.common.api.vo.InitTenantMenu;


@FeignClient(name = "buyi-system")
public interface IMenuServiceFeign {

    /**
     * 初始化租户菜单信息
     * @param initTenantMenu 初始化租户菜单信息
     * @return 初始化租户菜单信息
     */
    @PostMapping("/sys-menu/initTenantMenu")
    CommonResult<?> initTenantMenu(@RequestBody InitTenantMenu initTenantMenu);

    /**
     * 初始化角色与菜单的关联关系
     * @param initRoleJoinMenuVo 角色与菜单的关联关系
     * @return  Result<?>
     */
    @PostMapping("/sys-role-join-menu/initRoleJoinMenu")
    CommonResult<?> initRoleJoinMenu(@RequestBody InitRoleJoinMenu initRoleJoinMenuVo);
}
