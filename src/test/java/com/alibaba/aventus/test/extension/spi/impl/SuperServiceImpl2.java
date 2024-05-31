package com.alibaba.aventus.test.extension.spi.impl;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/24 16:34.
 */
public class SuperServiceImpl2 extends SuperServiceBaseImpl {

    @Override
    public String handle2(String arg) {
        return "SuperServiceImpl2 handle2: " + arg;
    }
}
