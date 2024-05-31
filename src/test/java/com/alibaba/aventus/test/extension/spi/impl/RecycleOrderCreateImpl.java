package com.alibaba.aventus.test.extension.spi.impl;

import com.alibaba.aventus.extension.annotation.ExtensionBusiness;
import com.alibaba.aventus.test.extension.spi.OrderCreateService;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/30 17:40.
 */
@ExtensionBusiness(code = "recycle, huishou, hs")
public class RecycleOrderCreateImpl implements OrderCreateService {

    @Override
    public String handle(String arg) {
        return "RecycleOrderCreateImpl handle: " + arg;
    }
}
