package com.alibaba.aventus.test.extension.spi.impl;

import java.util.function.Supplier;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 23:26.
 */
public class BaseSupplierImpl implements Supplier<Object> {

    @Override
    public Object get() {
        return "BaseSupplierImpl get()";
    }
}
