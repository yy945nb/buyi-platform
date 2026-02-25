package com.buyi.common.redis.constant;

/**
 * Redis Key常量
 */
public interface RedisConstants {

    /**
     * 系统配置Redis-key
     */
    String SYSTEM_CONFIG_KEY = "system:config";

    /**
     * IP限流Redis-key
     */
    String IP_RATE_LIMITER_KEY = "ip:rate:limiter:";

    /**
     * 防重复提交Redis-key
     */
    String RESUBMIT_LOCK_PREFIX = "resubmit:lock:";

    /**
     * 单个IP请求的最大每秒查询数（QPS）阈值Key
     */
    String IP_QPS_THRESHOLD_LIMIT_KEY = "IP_QPS_THRESHOLD_LIMIT";

    /**
     * 手机验证码缓存前缀
     */
    String MOBILE_VERIFICATION_CODE_PREFIX = "VERIFICATION_CODE:MOBILE:";

    /**
     * 为字典表数据缓存时，缓存名称的固定后缀。
     */
     String DICT_CACHE_NAME_SUFFIX = "-DICT";

     String GATEWAY_ROUTE_KEY = "GATEWAY:ROUTE";

    // 用户token存储
    String USER_TOKEN = "USER_TOKEN";

    // 短信验证码
    String SMS_CODE_PREFIX = "sms:code:";

}
