package com.alibaba.aventus.test.extension.spi.impl;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 23:29.
 */
public class YhbSupplierImpl extends BaseSupplierImpl {
    @Override
    public Object get() {
        return "YhbSupplierImpl get()";
    }
}
