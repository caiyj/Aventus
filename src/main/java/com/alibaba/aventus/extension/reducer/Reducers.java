package com.alibaba.aventus.extension.reducer;

import java.util.function.Predicate;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 22:31.
 */
public class Reducers {


    /**
     * 返回满足条件的第一个结果
     *
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> FirstOf<T> firstOf(Predicate<T> predicate) {
        return new FirstOf<>(predicate);
    }

    private static final FirstOf firstOf = new FirstOf();

    public static <T> FirstOf<T> firstOf() {
        return (FirstOf<T>) firstOf;
    }

    /**
     * 返回满足条件的所有结果
     *
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> Collect<T> collect(Predicate<T> predicate) {
        return new Collect<>(predicate);
    }

    private static final Collect collect = new Collect();

    public static <T> Collect<T> collect() {
        return (Collect<T>) collect;
    }

    /**
     * 返回是否有任意一个结果满足条件
     *
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> AnyMatch<T> anyMatch(Predicate<T> predicate) {
        return new AnyMatch<>(predicate);
    }

    /**
     * 返回是否所有结果都满足条件
     *
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> AllMatch<T> allMatch(Predicate<T> predicate) {
        return new AllMatch<>(predicate);
    }

    /**
     * 返回是否所有结果都不满足条件
     *
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> NoneMatch<T> noneMatch(Predicate<T> predicate) {
        return new NoneMatch<>(predicate);
    }

}
