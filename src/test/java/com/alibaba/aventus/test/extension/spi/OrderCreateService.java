package com.alibaba.aventus.test.extension.spi;

import com.alibaba.aventus.extension.annotation.Extension;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/30 17:37.
 */
@Extension(desc = "订单创建扩展")
public interface OrderCreateService {

    String handle(String arg);

}
