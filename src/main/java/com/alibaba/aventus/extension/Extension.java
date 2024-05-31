package com.alibaba.aventus.extension;

import com.alibaba.aventus.extension.core.ExtensionExecutor;
import com.alibaba.aventus.extension.core.ExtensionManager;
import com.alibaba.aventus.extension.reducer.Reducer;
import com.alibaba.aventus.extension.reducer.Reducers;
import com.alibaba.aventus.logging.AventusLogging;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.alibaba.aventus.extension.ExtensionParser.BASE_EXTENSION_GROUP;
import static com.alibaba.aventus.extension.ExtensionParser.BASE_EXTENSION_ROUTER;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 22:31.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class Extension {

    private static final ConcurrentMap<Class<?>, ExtensionParser> parsers = new ConcurrentHashMap<>();

    private Extension() {
    }

    public static <SPI, R> R execute(Class<SPI> spi, ExtensionInvoker<SPI, R> invoker) {
        return ExtensionExecutor.execute(BASE_EXTENSION_GROUP, spi, invoker, Reducers.firstOf());
    }

    public static <SPI, T, R> R execute(Class<SPI> spi, ExtensionInvoker<SPI, T> invoker, Reducer<T, R> reducer) {
        return ExtensionExecutor.execute(BASE_EXTENSION_GROUP, spi, invoker, reducer);
    }

    /*
     * 分组执行: 带有group参数
     */
    public static <SPI, R> R execute(String group, Class<SPI> spi, ExtensionInvoker<SPI, R> invoker) {
        return ExtensionExecutor.execute(group, spi, invoker, Reducers.firstOf());
    }

    public static <SPI, T, R> R execute(String group, Class<SPI> spi, ExtensionInvoker<SPI, T> invoker, Reducer<T, R> reducer) {
        return ExtensionExecutor.execute(group, spi, invoker, reducer);
    }


    public static <SPI, T, R> R execute_(String code, Class<SPI> spi, ExtensionInvoker<SPI, T> invoker, Reducer<T, R> reducer) {
        ExtensionContext.setExtensionRouter(BASE_EXTENSION_GROUP, BASE_EXTENSION_ROUTER);
        ExtensionContext.setExtensionCode(BASE_EXTENSION_GROUP, code);
        try {
            return ExtensionExecutor.execute(BASE_EXTENSION_GROUP, spi, invoker, reducer);
        } finally {
            clear();
        }
    }

    /**
     * 解析扩展身份:ExtensionCode、(扩展分组:ExtensionGroup、扩展路由:ExtensionRouter)
     *
     * @param parser
     * @param param
     * @param <T>
     * @return
     */
    public static <T> String parse(Class<? extends ExtensionParser<T>> parser, T param) {

        ExtensionParser<T> instance = parsers.computeIfAbsent(parser, clazz -> {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return (ExtensionParser) constructor.newInstance();
            } catch (InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                throw Throwables.propagate(e);
            }
        });

        String group = instance.parseExtensionGroup(param);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(group));

        ExtensionRouter router = instance.getExtensionRouter(param);
        Preconditions.checkArgument(router != null);
        ExtensionContext.setExtensionRouter(group, router);

        String code = instance.parseExtensionCode(param);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(code));
        ExtensionContext.setExtensionCode(group, code);

        return code;
    }

    public static void clear() {
        ExtensionExecutor.clear();
        ExtensionContext.clear();
    }

    public static void register() throws Exception {
        AventusLogging.aventus.info("[AVENTUS-EXTENSION] Starting...");
        ExtensionManager.registerExtensionPlugins();
        ExtensionManager.registerExtensionMap();
    }
}
