package com.alibaba.aventus.extension.reducer;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * 返回满足条件的第一个结果
 *
 * @author qingfei
 * @date 2022/05/19
 */
public class FirstOf<T> implements Reducer<T, T> {

    private final Predicate<T> predicate;

    public FirstOf() {
        this(null);
    }

    public FirstOf(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean willBreak(T item) {
        return predicate == null || predicate.test(item);
    }

    @Override
    public T reduce(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        if (predicate == null) {
            return collection.iterator().next();
        }

        for (T item : collection) {
            if (predicate.test(item)) {
                return item;
            }
        }

        return null;
    }

    @Override
    public boolean isSameType() {
        return true;
    }
}
