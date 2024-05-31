package com.alibaba.aventus.test.extension.spi.impl;

import com.alibaba.aventus.extension.annotation.ExtensionBusiness;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/30 17:39.
 */
@ExtensionBusiness(code = "yhb")
public class YhbOrderCreateImpl extends OrderCreateServiceBaseImpl {

    @Override
    public String handle(String arg) {
        return "YhbOrderCreateImpl" + super.handle(arg);
    }
}
