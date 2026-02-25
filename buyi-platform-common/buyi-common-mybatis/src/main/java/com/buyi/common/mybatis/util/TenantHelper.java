package com.buyi.common.mybatis.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 */
@Slf4j
public class TenantHelper {
    private static final ThreadLocal localParameters = new ThreadLocal();

    public static <T> T get(){
        T t = (T) localParameters.get();
        log.info("TenantHelper ThreadID:{}, threadLocal {}", Thread.currentThread().getId(), JSON.toJSONString(t));
        return t;
    }

    public static <T> void set(T t){
        log.info("TenantHelper ThreadID:{}, threadLocal set {}", Thread.currentThread().getId(), JSON.toJSONString(t));
        localParameters.set(t);
    }
}
