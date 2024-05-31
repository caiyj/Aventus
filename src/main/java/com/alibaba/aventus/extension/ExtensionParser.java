package com.alibaba.aventus.extension;

import com.alibaba.aventus.extension.core.BaseExtensionRouter;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 22:31.
 */
public interface ExtensionParser<Param> {


    String BASE_EXTENSION_GROUP = "BASE";

    ExtensionRouter BASE_EXTENSION_ROUTER = new BaseExtensionRouter();

    default String parseExtensionGroup(Param param) {
        return BASE_EXTENSION_GROUP;
    }

    default ExtensionRouter getExtensionRouter(Param param) {
        return BASE_EXTENSION_ROUTER;
    }


    String parseExtensionCode(Param param);
}
