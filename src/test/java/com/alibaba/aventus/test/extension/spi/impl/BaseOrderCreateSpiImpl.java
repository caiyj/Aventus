package com.alibaba.aventus.test.extension.spi.impl;

import com.alibaba.aventus.extension.annotation.ExtensionBase;
import com.alibaba.aventus.test.extension.domain.OrderCreateParam;
import com.alibaba.aventus.test.extension.spi.OrderCreateSpi;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 17:27.
 */
@Component
public class BaseOrderCreateSpiImpl implements OrderCreateSpi {

    @Override
    public boolean isUseRedPacked(OrderCreateParam param) {
        return false;
    }

    @Override
    public Map<String, Object> getCustomOrderAttributes(OrderCreateParam param) {
        return Collections.emptyMap();
    }
}
