package com.buyi.common.mybatis.listen;

import java.util.List;


public interface QueryListener<T> {
    default List<T> queryList() {
        return null;
    }

    default T queryById(String id) {
        return null;
    }
}
