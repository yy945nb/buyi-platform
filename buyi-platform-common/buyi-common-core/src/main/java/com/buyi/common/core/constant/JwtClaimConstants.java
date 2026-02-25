package com.buyi.common.core.constant;

/**
 * JWT Claims声明常量
 * <p>
 * JWT Claims 属于 Payload 的一部分，包含了一些实体（通常指的用户）的状态和额外的元数据。
 *
 */
public interface JwtClaimConstants {

    /**
     * 用户ID
     */
    String USER_ID = "userId";

    /**
     * 部门ID
     */
    String DEPT_ID = "deptId";

    String COMPANY_ID = "companyId";

    String TENENT_ID = "tenent_id";

    /**
     * 数据权限
     */
    String DATA_SCOPE = "dataScope";

    /**
     * 权限(角色Code)集合
     */
    String AUTHORITIES = "authorities";

}
