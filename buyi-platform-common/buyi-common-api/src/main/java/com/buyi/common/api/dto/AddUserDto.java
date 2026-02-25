package com.buyi.common.api.dto;

import com.buyi.common.core.model.dto.BaseDto;
import lombok.Data;

import java.io.Serializable;


@Data
public class AddUserDto extends BaseDto {

    private static final long serialVersionUID = -596668932026414547L;

    private String name;

    private String loginName;

    private String password;

    private String workNum;

    private String nameFirstLetter;

    private String email;

    private String phone;

    private String mobile;

    private String postId;

    private String photo;

    private String tenantId;
}
