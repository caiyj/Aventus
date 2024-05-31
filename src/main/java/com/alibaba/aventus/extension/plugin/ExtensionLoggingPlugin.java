package com.alibaba.aventus.extension.plugin;

import com.alibaba.aventus.extension.ExtensionContext;

import static com.alibaba.aventus.logging.AventusLogging.extensionLogger;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/15 20:25.
 */
public class ExtensionLoggingPlugin implements ExtensionPlugin {

    @Override
    public Object invoke(ExtensionInvocation invocation) throws Exception {
        long start = System.currentTimeMillis();
        String group = invocation.getGroup();
        String spi = invocation.getExtensionSpi().getName();
        String method = invocation.getExtensionMethod().getName();
        String router = ExtensionContext.getExtensionRouter(group).getClass().getSimpleName();
        String bizCode = ExtensionContext.getExtensionCode(group);
        String type = invocation.getType();
        Object instance = invocation.getInstance();
        Object[] args = invocation.getArgs();
        Object result = null;
        Throwable except = null;
        try {
            return (result = invocation.proceed());
        } catch (Throwable t) {
            except = t;
            throw t;
        } finally {
            long rt = System.currentTimeMillis() - start;
            if (except == null) {
                extensionLogger.info("{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|", group, spi, method, router, bizCode, type, instance, getArgs(args), getResult(result),
                        rt);
            } else {
                extensionLogger.error("{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|", group, spi, method, router, bizCode, type, instance, getArgs(args), getResult(result)
                        , rt, except);
            }
        }
    }

    protected Object getArgs(Object[] args) {
        return "";
    }

    protected Object getResult(Object result) {
        return "";
    }
}
