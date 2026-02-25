package com.buyi.common.mybatis.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @description: 测试数据库连接
 * @author: fgw
 * @createDate: 2024/1/16
 */
@Slf4j
public class TestConnectionUtil {

    /**
     * 测试数据库连接
     * @param url 数据库连接地址
     * @param username 用户名
     * @param password 密码
     * @return 是否连接成功
     */
    public static Boolean testConnection(String url, String username, String password,String driverClassName) {
        try {
            Class.forName(driverClassName);
            Connection connection = DriverManager.getConnection(url, username, password);
            if (connection != null && !connection.isClosed()) {
                log.info("成功连接到MySQL数据库！");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT VERSION() as version;");

                while (resultSet.next()) {
                    String version = resultSet.getString("version");
                    return StrUtil.isNotEmpty(version);
                }
                resultSet.close();
                statement.close();
                connection.close();
            } else {
                log.info("无法连接到MySQL数据库。");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
