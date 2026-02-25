package com.buyi.common.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @description: 初始化租户管理员
 */
@Data
public class InitAdminUserVo {

    @Schema(description = "名称")
    private String id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "租户id")
    private String tenantId;

    @Schema(description = "登录名")
    private String loginName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "姓名首字母")
    private String nameFirstLetter;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "是否可删除0：可删除  1：不可删除")
    private String isDeletable;

    public  InitAdminUserVo buildAdminUser(String name,String tenantId,String phone){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.name = name;
        this.tenantId = tenantId;
        this.loginName = "admin";
        this.phone = phone;
        this.isDeletable = "1";
        this.password = passwordEncoder.encode("123456");
        return this;
    }
}
