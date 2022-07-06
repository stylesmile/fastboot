package com.example.demo.tool;

import java.util.Collection;

/**
 * 集合判断空
 *
 * @author chenye
 */
public class CollectionUtil {
    public static <T> boolean isEmpty(Collection<T> datas) {
        return datas == null || datas.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> datas) {
        return !isEmpty(datas);
    }
}
