package com.alibaba.aventus.extension.proxy.jdk;

import com.alibaba.aventus.extension.core.ExtensionExecutor;
import com.alibaba.aventus.extension.reducer.Reducer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 22:49.
 */
@SuppressWarnings("unchecked")
public class JdkProxyFactory<SPI> implements InvocationHandler {

    private final String group;

    private final Class<SPI> spi;

    private final Reducer<?, ?> reducer;

    public JdkProxyFactory(String group, Class<SPI> spi, Reducer<?, ?> reducer) {
        this.group = group;
        this.spi = spi;
        this.reducer = reducer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (ReflectionUtils.isObjectMethod(method)) {
            return method.invoke(this, args);
        }
        return ExtensionExecutor._execute(group, spi, method, args, reducer);
    }

    public static <SPI> SPI newProxy(String group, Class<SPI> spi, Reducer<?, ?> reducer) {
        return (SPI) Proxy.newProxyInstance(spi.getClassLoader(), new Class[]{spi}, new JdkProxyFactory<>(group, spi, reducer));
    }
}
