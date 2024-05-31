package com.alibaba.aventus.test.extension.spi.impl;

import com.alibaba.aventus.test.extension.spi.SuperService;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/24 16:34.
 */
public class SuperServiceBaseImpl implements SuperService {

    @Override
    public String handle1(String arg) {
        return "SuperServiceBaseImpl handle1: " + arg;
    }

    @Override
    public String handle2(String arg) {
        return "SuperServiceBaseImpl handle2: " + arg;
    }
}
