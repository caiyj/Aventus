package com.alibaba.aventus.extension.plugin;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/12 11:20.
 */
public interface ExtensionPlugin {

    Object invoke(ExtensionInvocation invocation) throws Exception;
}
