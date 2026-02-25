package com.buyi.common.mybatis.handler;


import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import net.sf.jsqlparser.expression.Expression;
import org.springframework.stereotype.Component;

/**
 * @description: 数据权限处理器
 */
@Component
public class CustomizeDataPermissionHandler  implements DataPermissionHandler {

    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {

        try {
            Class<?> clazz  = Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf(".")));
            String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(".") + 1);
//            Method declaredMethod = clazz.getDeclaredMethod(methodName);
//            DataPermission annotation = declaredMethod.getAnnotation(DataPermission.class);
            System.out.println("where = " + where);
            System.out.println("mappedStatementId = " + mappedStatementId);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return where;
    }
}
