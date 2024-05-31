package com.alibaba.aventus.extension.proxy;

import com.alibaba.aventus.extension.proxy.cglib.CglibProxyFactory;
import com.alibaba.aventus.extension.proxy.jdk.JdkProxyFactory;
import com.alibaba.aventus.extension.proxy.spring.SpringProxyFactory;
import com.alibaba.aventus.extension.reducer.Reducer;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 22:31.
 */
public class ProxyFactory {

    private static volatile String type = null;

    public static <SPI> SPI newProxy(String group, Class<SPI> spi) {
        return newProxy(group, spi, null);
    }

    public static <SPI> SPI newProxy(String group, Class<SPI> spi, Reducer<?, ?> reducer) {
        if (type != null) {
            switch (type) {
                case "spring":
                    return SpringProxyFactory.newProxy(group, spi, reducer);
                case "cglib":
                    return CglibProxyFactory.newProxy(group, spi, reducer);
                case "jdk":
                    return JdkProxyFactory.newProxy(group, spi, reducer);
            }
        }

        try {
            Class.forName("org.springframework.cglib.proxy.Enhancer");
            type = "spring";
            return SpringProxyFactory.newProxy(group, spi, reducer);
        } catch (ClassNotFoundException ignored) {
        }

        try {
            Class.forName("net.sf.cglib.proxy.Enhancer");
            type = "cglib";
            return CglibProxyFactory.newProxy(group, spi, reducer);
        } catch (ClassNotFoundException ignored) {
        }

        type = "jdk";
        return JdkProxyFactory.newProxy(group, spi, reducer);
    }
}
