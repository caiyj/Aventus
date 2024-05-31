package com.alibaba.aventus.test.extension.spi.impl;

import com.alibaba.aventus.extension.annotation.ExtensionBusiness;
import com.alibaba.aventus.test.extension.domain.OrderCreateParam;

import java.util.Collections;
import java.util.Map;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 17:32.
 */
@ExtensionBusiness(code = "o2o", desc = "O2O交易")
public class O2OOrderCreateSpiImpl extends BaseOrderCreateSpiImpl {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return false;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.singletonMap("order-source", "o2o");
    }
}
