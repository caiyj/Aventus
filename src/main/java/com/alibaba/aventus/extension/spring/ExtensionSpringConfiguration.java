package com.alibaba.aventus.extension.spring;

import com.alibaba.aventus.extension.Extension;
import com.alibaba.aventus.extension.factory.SpringBeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 23:13.
 */
@Configuration
public class ExtensionSpringConfiguration implements InitializingBean, ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanFactory.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Extension.register();
    }
}
