package com.alibaba.aventus.test.extension.spi.impl;

import com.alibaba.aventus.test.extension.domain.OrderCreateParam;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 17:29.
 */
@Component
public class NormalOrderCreateSpiImpl extends BaseOrderCreateSpiImpl {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return true;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.singletonMap("order-source", "normal");
    }
}
