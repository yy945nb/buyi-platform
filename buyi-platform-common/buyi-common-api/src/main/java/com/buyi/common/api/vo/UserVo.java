package com.buyi.common.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 查询用户结果
 */
@Data
public class UserVo implements Serializable {
    private static final long serialVersionUID = -596668932026414547L;

    private String id;

    private String name;

    private String password;

}
