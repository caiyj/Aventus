package com.alibaba.aventus.extension.reducer;

import java.util.Collection;

/**
 * T是扩展点执行的结果类型
 * R是reducer后的结果类型
 *
 * @author qingfei
 * @date 2022/05/19
 */
public interface Reducer<T, R> {

    /**
     * 是否中断方法执行
     *
     * @param item
     * @return
     */
    boolean willBreak(T item);

    /**
     * reduce方法
     *
     * @param collection
     * @return
     */
    R reduce(Collection<T> collection);

    default boolean isSameType() {
        return false;
    }
}
