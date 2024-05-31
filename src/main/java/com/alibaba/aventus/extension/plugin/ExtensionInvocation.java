package com.alibaba.aventus.extension.plugin;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/12 11:21.
 */
public class ExtensionInvocation {

    @Getter
    private final String group;

    @Getter
    private final Class<?> extensionSpi;

    @Getter
    @Setter
    private Method extensionMethod;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private Object instance;

    @Getter
    @Setter
    private Object[] args;

    private final ExtensionPlugin[] plugins;

    public ExtensionInvocation(String group, Class<?> extensionSpi, Method extensionMethod, String type, Object instance, Object[] args, ExtensionPlugin[] plugins) {
        this.group = group;
        this.extensionSpi = extensionSpi;
        this.extensionMethod = extensionMethod;
        this.type = type;
        this.instance = instance;
        this.args = args;
        this.plugins = plugins;
    }

    private int idx = -1;

    public Object proceed() throws Exception {
        if (++idx < plugins.length) {
            return plugins[idx].invoke(this);
        } else {
            return extensionMethod.invoke(instance, args);
        }
    }
}
