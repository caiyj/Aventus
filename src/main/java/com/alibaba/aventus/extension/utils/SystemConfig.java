package com.alibaba.aventus.extension.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 21:07.
 */
public class SystemConfig {

    // todo: 优化取名
    public static boolean isGlobalCloseLazyLoading() {
        if (StringUtils.equalsIgnoreCase(System.getProperty("spring.profiles.active"), "production")) {
            return true;
        }
        if (StringUtils.equalsIgnoreCase(System.getProperty("aventus.extension.turnoff.lazy.loading"), "true")) {
            return true;
        }

        return false;
    }

    public static boolean isGlobalOpenLazyLoading() {
        if (StringUtils.equalsIgnoreCase(System.getProperty("spring.profiles.active"), "production")) {
            return false;
        }
        if (StringUtils.equalsIgnoreCase(System.getProperty("aventus.extension.turnon.lazy.loading"), "true")) {
            return true;
        }

        return false;
    }
}
