package io.github.stylesmile.tool;

import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * 集合判断空
 *
 * @author chenye
 */
public class CollectionUtil {
    public static <T> boolean isEmpty(Collection<T> datas) {
        return datas == null || datas.isEmpty();
    }
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> datas) {
        return !isEmpty(datas);
    }
}
