package com.alibaba.aventus.extension.spring;

import com.alibaba.aventus.extension.core.ExtensionManager;
import com.alibaba.aventus.extension.domain.Entity;
import com.alibaba.aventus.extension.reducer.Reducers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/30 15:43.
 */
@Slf4j
@Component
public class ExtensionSpringBeanAutoRegister implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (Map.Entry<Class<?>, Entity.Extension> entry : ExtensionManager.getExtensionMap().entrySet()) {
            if (!entry.getValue().proxy) {
                continue;
            }

            Class<?> spi = entry.getKey();

            RootBeanDefinition definition = new RootBeanDefinition();
            definition.setBeanClass(ExtensionSpringBean.class);
            definition.getPropertyValues().addPropertyValue("spi", spi);
            definition.getPropertyValues().addPropertyValue("reducer", Reducers.firstOf());
            definition.setPrimary(true);

            String beanName = "aventus" + spi.getSimpleName();
            registry.registerBeanDefinition(beanName, definition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }
}
