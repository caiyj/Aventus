package com.alibaba.aventus.extension.reducer;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/19 08:19.
 */
public class NoneMatch<T> implements Reducer<T, Boolean> {

    private final Predicate<T> predicate;

    public NoneMatch(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public boolean willBreak(T item) {
        return predicate.test(item);
    }

    @Override
    public Boolean reduce(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return true;
        }

        for (T item : collection) {
            if (predicate.test(item)) {
                return false;
            }
        }

        return true;
    }
}
