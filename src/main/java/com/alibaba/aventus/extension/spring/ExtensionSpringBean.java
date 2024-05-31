package com.alibaba.aventus.extension.spring;

import com.alibaba.aventus.extension.ExtensionParser;
import com.alibaba.aventus.extension.proxy.ProxyFactory;
import com.alibaba.aventus.extension.reducer.Reducer;
import com.google.common.base.Preconditions;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/1/26 22:34.
 */
@Slf4j
public class ExtensionSpringBean<SPI> implements FactoryBean<SPI> {

    @Setter
    private Class<SPI> extension;

    @Setter
    private Reducer<?, ?> reducer;

    @Setter
    private boolean singleton = true;

    @Override
    public SPI getObject() {
        Preconditions.checkState(extension != null && reducer != null && reducer.isSameType());
        SPI proxy = ProxyFactory.newProxy(ExtensionParser.BASE_EXTENSION_GROUP, extension, reducer);
        log.info("Register spi:[{}] reducer:[{}] spring-proxy-bean:[{}] into spring context.", extension, reducer, proxy);
        return proxy;
    }

    @Override
    public Class<SPI> getObjectType() {
        return extension;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }
}
