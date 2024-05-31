package com.alibaba.aventus.extension.reducer;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 满足条件的返回值收集
 *
 * @author qingfei
 * @date 2022/05/19
 */
public class Collect<T> implements Reducer<T, List<T>> {

    private final Predicate<T> predicate;

    public Collect() {
        this(null);
    }

    public Collect(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean willBreak(T item) {
        return false;
    }

    @Override
    public List<T> reduce(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(collection.size());
        for (T item : collection) {
            if (predicate == null || predicate.test(item)) {
                list.add(item);
            }
        }
        return list;
    }
}
