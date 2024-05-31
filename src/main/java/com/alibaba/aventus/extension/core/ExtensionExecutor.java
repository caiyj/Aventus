package com.alibaba.aventus.extension.core;

import com.alibaba.aventus.extension.ExtensionContext;
import com.alibaba.aventus.extension.ExtensionInvoker;
import com.alibaba.aventus.extension.domain.SpiImpls;
import com.alibaba.aventus.extension.exception.ExtensionException;
import com.alibaba.aventus.extension.plugin.ExtensionInvocation;
import com.alibaba.aventus.extension.proxy.ProxyFactory;
import com.alibaba.aventus.extension.reducer.Reducer;
import com.alibaba.aventus.extension.utils.SysNamespace;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.alibaba.aventus.extension.core.ExtensionManager.getPlugins;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 17:47.
 */
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class ExtensionExecutor {

    private static final ConcurrentMap<String, Object> proxies = new ConcurrentHashMap<>();

    private static final ThreadLocal<Map<String, Wrapper>> group2wrapper = ThreadLocal.withInitial(HashMap::new);

    public static <SPI, T, R> R execute(String group, Class<SPI> spi, ExtensionInvoker<SPI, T> invoker, Reducer<T, R> reducer) {
        try {
            Wrapper wrapper = new Wrapper();
            wrapper.reducer = reducer;
            group2wrapper.get().put(group, wrapper);

            Object result = invoker.invoke((SPI) proxies.computeIfAbsent(group + ":" + spi, _K -> ProxyFactory.newProxy(group, spi)));
            if (reducer.isSameType()) {
                return (R) result;
            } else {
                return (R) wrapper.result;
            }
        } finally {
            group2wrapper.get().remove(group);
        }
    }

    public static <SPI> Object _execute(String group, Class<SPI> spi, Method method, Object[] args, Reducer reducer) throws Throwable {
        if (!spi.isInterface()) {
            throw new ExtensionException(String.format("ExtensionSpi:[%s] is not interface.", spi));
        }

        try {
            ExtensionContext.addBusinessExt(group, SysNamespace.SPI, spi);
            ExtensionContext.addBusinessExt(group, SysNamespace.METHOD, method.getName());
            reducer = reducer != null ? reducer : group2wrapper.get().get(group).reducer;

            SpiImpls impls = ExtensionManager.getSpiImpls(group, spi, args);
            List<Object> results = new ArrayList<>(impls.size());

            for (SpiImpls.SpiImpl impl : impls) {
                Object result = invoke(group, spi, impl, method, args);
                results.add(result);
                if (reducer.willBreak(result)) {
                    break;
                }
            }

            Object result = reducer.reduce(results);
            if (reducer.isSameType()) {
                return result;
            } else {
                group2wrapper.get().get(group).result = result;
                return null;
            }
        } finally {
            ExtensionContext.removeBusinessExt(group, SysNamespace.METHOD);
            ExtensionContext.removeBusinessExt(group, SysNamespace.SPI);
        }
    }

    private static <SPI> Object invoke(String group, Class<SPI> spi, SpiImpls.SpiImpl impl, Method method, Object[] args) throws Exception {
        method.setAccessible(true);
        return new ExtensionInvocation(group, spi, method, impl.type, impl.instance, args, getPlugins()).proceed();
    }

    private static class Wrapper {

        Reducer<?, ?> reducer;

        Object result;
    }

    public static void clear() {
        group2wrapper.remove();
    }
}
