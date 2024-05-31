package com.alibaba.aventus.test.extension.spi;

import com.alibaba.aventus.extension.annotation.Extension;
import com.alibaba.aventus.test.extension.domain.OrderCreateParam;

import java.util.Map;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 17:24.
 */
public interface OrderCreateSpi {

    boolean isUseRedPacked(OrderCreateParam param);

    Map<String, Object> getCustomOrderAttributes(OrderCreateParam param);
}
