package com.buyi.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>{@link BeanConvertUtils}</code>
 * <p>
 * description: BeanConvertUtils
 * <p>
 */
@Slf4j
public class BeanConvertUtils {

    public static <S,T> List<T> copyBeanList(List<S> sources, Class<T> target, CallbackHook<S,T> callbackHook){
        List<T> list = new ArrayList<T>();;
        if(CollectionUtils.isEmpty(sources)){
            return list;
        }
        try {
            for(S bean:sources){
                T t = target.newInstance();
                BeanUtils.copyProperties(bean,t);
                callbackHook.hook(bean,t);
                list.add(t);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        return list;

    }

    public static <S,T> List<T> copyBeanList(List<S> sources, Class<T> target){
        List<T> list = new ArrayList<T>();;
        if(CollectionUtils.isEmpty(sources)){
            return list;
        }
        try {
            for(S bean:sources){
                T t = target.newInstance();
                BeanUtils.copyProperties(bean,t);
                list.add(t);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return list;

    }

    public static <S,T> T copyBean(S source, Class<T> target, CallbackHook<S,T> callbackHook){
        if(source == null){
            return null;
        }
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source,t);
            callbackHook.hook(source,t);
            return t;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;

    }

    public static <S,T> T copyBean(S source, Class<T> target){
        if(source == null){
            return null;
        }
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source,t);
            return t;
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }
}
