package com.alibaba.aventus.test.extension.spi.impl;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 23:28.
 */
public class YhbFunctionImpl extends BaseFunctionImpl {

    @Override
    public Object apply(Object arg) {
        if (arg != null) {
            System.out.println("argType: " + arg.getClass());
        }
        return "YhbFunctionImpl apply(arg)";
    }
}
