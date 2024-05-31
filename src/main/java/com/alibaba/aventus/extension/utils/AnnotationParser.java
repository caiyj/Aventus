package com.alibaba.aventus.extension.utils;

import com.alibaba.aventus.extension.annotation.Extension;
import com.alibaba.aventus.extension.annotation.ExtensionBase;
import com.alibaba.aventus.extension.annotation.ExtensionBusiness;
import com.alibaba.aventus.extension.domain.Entity;
import com.alibaba.aventus.extension.exception.ExtensionException;
import com.alibaba.aventus.extension.factory.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;

import java.util.*;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/30 16:45.
 */
@Slf4j
public class AnnotationParser {

    public static Map<Class<?>, Entity.Extension> parse() {
        Set<Class<?>> extensions = getExtensions();
        Map<Class<?>, Entity.Extension> extensionMap = new HashMap<>(extensions.size());


        for (Class<?> extension : extensions) {
            Object base = getBaseImpl(extension);
            Map<String, List<Entity.Business>> businessMap = toBusinessMap(extension);

            Entity.Extension extensionEntity = new Entity.Extension(extension.getName(), base, businessMap);
            extensionEntity.proxy = extension.getAnnotation(Extension.class).proxy();

            extensionMap.put(extension, extensionEntity);
        }

        return extensionMap;
    }

    private static Object getBaseImpl(Class<?> extension) {
        for (Object bean : SpringBeanFactory.applicationContext.getBeansWithAnnotation(ExtensionBase.class).values()) {
            if (extension.isInstance(bean) && AopUtils.getTargetClass(bean).isAnnotationPresent(ExtensionBase.class)) {
                return bean;
            }
        }

        throw new ExtensionException("could not found base impl for extension:[" + extension + "]");
    }

    private static Map<String, List<Entity.Business>> toBusinessMap(Class<?> extension) {
        Map<String, List<Pair<Object, ExtensionBusiness>>> code2beans = new HashMap<>();
        for (Object bean : SpringBeanFactory.applicationContext.getBeansWithAnnotation(ExtensionBusiness.class).values()) {
            if (extension.isInstance(bean)) {
                ExtensionBusiness business = AopUtils.getTargetClass(bean).getAnnotation(ExtensionBusiness.class);
                for (String code : business.code()) {
                    code2beans.computeIfAbsent(code, _k -> new ArrayList<>()).add(Pair.of(bean, business));
                }
            }
        }

        Map<String, List<Entity.Business>> code2businessMap = new HashMap<>(code2beans.size());

        for (Map.Entry<String, List<Pair<Object, ExtensionBusiness>>> entry : code2beans.entrySet()) {
            String code = entry.getKey();
            List<Pair<Object, ExtensionBusiness>> pairs = entry.getValue();

            pairs.sort(Comparator.comparingInt(pair -> pair.getRight().priority()));

            List<Entity.Business> businessList = new ArrayList<>(pairs.size());
            for (Pair<Object, ExtensionBusiness> pair : pairs) {
                Object instance = pair.getLeft();
                ExtensionBusiness business = pair.getRight();
                businessList.add(Entity.Business.newBeanInstance(code, "bean", business.priority(), null, instance));
            }
            code2businessMap.put(code, businessList);
        }

        return code2businessMap;
    }


    private static Set<Class<?>> getExtensions() {
        Set<Class<?>> extensions = new HashSet<>();
        for (Object bean : SpringBeanFactory.applicationContext.getBeansWithAnnotation(ExtensionBase.class).values()) {
            for (Class<?> clazz : ClassUtils.getAllInterfacesForClass(AopUtils.getTargetClass(bean))) {
                if (clazz.isAnnotationPresent(Extension.class) && !extensions.contains(clazz)) {
                    log.info("loaded Extension:[{}/{}] with annotation @Extension.", clazz.getName(), clazz.getAnnotation(Extension.class).desc());
                    extensions.add(clazz);
                }
            }
        }
        return extensions;
    }
}
