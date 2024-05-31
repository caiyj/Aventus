package com.alibaba.aventus.extension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.aventus.extension.ExtensionParser.BASE_EXTENSION_GROUP;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 22:31.
 */
@SuppressWarnings("unchecked")
public class ExtensionContext {

    private static final String SYS_SCOPE = "__SYS";

    private static final String BIZ_SCOPE = "__BIZ";

    private static final String _SYS_ROUTER_ = "__router__";

    private static final String _SYS_CODE_ = "__code__";

    // domain -> {k, v}
    private static final ThreadLocal<Map<String, Map<String, Object>>> ctx = ThreadLocal.withInitial(HashMap::new);

    // 业务路由 ...
    protected static void setExtensionRouter(String group, ExtensionRouter router) {
        setCtxVal(group, SYS_SCOPE, _SYS_ROUTER_, router);
    }

    public static ExtensionRouter getExtensionRouter(String group) {
        return getCtxVal(group, SYS_SCOPE, _SYS_ROUTER_);
    }

    public static void removeExtensionRouter(String group) {
        rmCtxVal(group, SYS_SCOPE, _SYS_ROUTER_);
    }

    // 业务身份 ...
    protected static void setExtensionCode(String group, String code) {
        setCtxVal(group, SYS_SCOPE, _SYS_CODE_, code);
    }

    public static String getExtensionCode() {
        return getExtensionCode(BASE_EXTENSION_GROUP);
    }

    public static String getExtensionCode(String group) {
        return getCtxVal(group, SYS_SCOPE, _SYS_CODE_);
    }

    public static void removeExtensionCode(String group) {
        rmCtxVal(group, SYS_SCOPE, _SYS_CODE_);
    }

    // 业务扩展 ...
    public static <Ext> void addBusinessExt(String group, String namespace, Ext ext) {
        setCtxVal(group, BIZ_SCOPE, namespace, ext);
    }

    public static <Ext> void addBusinessExt(String group, Ext ext) {
        addBusinessExt(group, ext.getClass().getName(), ext);
    }

    public static <Ext> Ext getBusinessExt(String group, String namespace) {
        return getCtxVal(group, BIZ_SCOPE, namespace);
    }

    public static <Ext> Ext getBusinessExt(String group, Class<Ext> ext) {
        return getBusinessExt(group, ext.getName());
    }

    public static void removeBusinessExt(String group, String namespace) {
        rmCtxVal(group, BIZ_SCOPE, namespace);
    }

    public static <Ext> void removeBusinessExt(String group, Ext ext) {
        removeBusinessExt(group, ext.getClass().getName());
    }

    public static void clear() {
        ctx.remove();
    }

    // helper ...
    private static void setCtxVal(String group, String scope, String key, Object val) {
        ctx.get().computeIfAbsent(group, _K -> new HashMap<>()).put(scope + ":" + key, val);
    }

    private static <T> T getCtxVal(String group, String scope, String key) {
        return (T) ctx.get().computeIfAbsent(group, _K -> new HashMap<>()).get(scope + ":" + key);
    }

    private static void rmCtxVal(String group, String scope, String key) {
        ctx.get().getOrDefault(group, Collections.emptyMap()).remove(scope + ":" + key);
    }
}
