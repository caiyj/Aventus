package com.alibaba.aventus.extension.reducer;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 所有条件都满足
 *
 * @author qingfei
 * @date 2022/05/19
 */
public class AllMatch<T> implements Reducer<T, Boolean> {

    private final Predicate<T> predicate;

    public AllMatch(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public boolean willBreak(T item) {
        return !predicate.test(item);
    }

    @Override
    public Boolean reduce(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return false;
        }
        for (T item : collection) {
            if (!predicate.test(item)) {
                return false;
            }
        }
        return true;
    }
}
