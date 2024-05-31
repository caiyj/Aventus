package com.alibaba.aventus.extension.core;

import com.alibaba.aventus.extension.ExtensionContext;
import com.alibaba.aventus.extension.ExtensionRouter;
import com.alibaba.aventus.extension.domain.SpiImpls;
import com.alibaba.aventus.extension.exception.ExtensionException;
import com.google.common.base.Strings;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/11/13 10:48.
 */
public class BaseExtensionRouter implements ExtensionRouter {

    @Override
    public SpiImpls route(String group, Class<?> spi, Object[] args) {

        String code = ExtensionContext.getExtensionCode(group);
        if (Strings.isNullOrEmpty(code)) {
            throw new ExtensionException("[ExtensionCode] can't be empty: please invoke Extension.parse(...) before.");
        }

        return ExtensionManager.getBusinessSpiImpls(spi, code);
    }
}
