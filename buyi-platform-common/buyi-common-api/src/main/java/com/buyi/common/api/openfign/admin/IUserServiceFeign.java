package com.buyi.common.api.openfign.admin;

import com.buyi.common.api.vo.InitAdminUserVo;
import com.buyi.common.api.vo.InitUserJoinRoleVo;
import com.buyi.common.api.vo.UserVo;
import com.buyi.common.core.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 */
@FeignClient(name = "buyi-system")
public interface IUserServiceFeign {
    /**
     * 根据用户名和租户id查询用户
     * @param username 用户名
     * @param tenantId 租户id
     * @return 用户信息
     */
    @GetMapping("/sys-user/selectByUsernameAndTenantId")
    CommonResult<UserVo> selectByUsernameAndTenantId(@RequestParam("username") String username, @RequestParam("tenantId") String tenantId);


    /**
     * 初始化租户管理员
     * @param initAdminUserVo 初始化租户管理员
     * @return 初始化租户管理员
     */
    @PostMapping("/sys-user/initTenantAdminUser")
    CommonResult<InitAdminUserVo> initTenantAdminUser(@RequestBody InitAdminUserVo initAdminUserVo);


    /**
     * 初始化用户与角色的关联关系
     * @param initUserJoinRoleVo 橘色与用户的关联关系
     * @return Result<?>
     */

    @PostMapping("/sys-role-join-user/initUserJoinRole")
    CommonResult<?> initUserJoinRole(@RequestBody InitUserJoinRoleVo initUserJoinRoleVo);

}
