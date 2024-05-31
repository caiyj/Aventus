package com.alibaba.aventus.extension;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 22:31.
 */
@FunctionalInterface
public interface ExtensionInvoker<T, R> {

    /**
     * 回调方法
     *
     * @param t
     * @return
     */
    R invoke(T t);

}
