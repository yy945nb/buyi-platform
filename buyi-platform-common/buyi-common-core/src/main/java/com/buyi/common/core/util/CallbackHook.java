package com.buyi.common.core.util;

/**
 * @author yiren
 */
public interface CallbackHook<S,T> {

    public void  hook(S s,T t);
}
