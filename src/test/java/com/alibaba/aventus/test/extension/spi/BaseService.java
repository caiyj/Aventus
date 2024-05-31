package com.alibaba.aventus.test.extension.spi;

import com.alibaba.aventus.extension.annotation.Extension;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/24 16:33.
 */
@Extension
interface BaseService {

    String handle1(String arg);
}
