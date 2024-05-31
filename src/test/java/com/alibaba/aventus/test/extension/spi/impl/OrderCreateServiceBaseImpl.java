package com.alibaba.aventus.test.extension.spi.impl;

import com.alibaba.aventus.extension.annotation.ExtensionBase;
import com.alibaba.aventus.test.extension.spi.OrderCreateService;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/30 17:37.
 */
@ExtensionBase
public class OrderCreateServiceBaseImpl implements OrderCreateService {

    @Override
    public String handle(String arg) {
        return "OrderCreateServiceBaseImpl handle: " + arg;
    }
}
