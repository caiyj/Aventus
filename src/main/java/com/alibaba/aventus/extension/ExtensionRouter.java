package com.alibaba.aventus.extension;

import com.alibaba.aventus.extension.domain.SpiImpls;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/11/13 10:42.
 */
public interface ExtensionRouter {

    SpiImpls route(String group, Class<?> spi, Object[] args);

}
