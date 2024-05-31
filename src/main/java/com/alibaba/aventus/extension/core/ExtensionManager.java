package com.alibaba.aventus.extension.core;

import com.alibaba.aventus.extension.ExtensionContext;
import com.alibaba.aventus.extension.ExtensionRouter;
import com.alibaba.aventus.extension.domain.Entity;
import com.alibaba.aventus.extension.domain.SpiImpls;
import com.alibaba.aventus.extension.exception.ExtensionException;
import com.alibaba.aventus.extension.factory.HsfServiceFactory;
import com.alibaba.aventus.extension.factory.SpringBeanFactory;
import com.alibaba.aventus.extension.plugin.ExtensionPlugin;
import com.alibaba.aventus.extension.utils.AnnotationParser;
import com.alibaba.aventus.extension.utils.DomParser;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 17:52.
 */
@Slf4j
public class ExtensionManager {

    private static Map<Class<?>, Entity.Extension> extensionMap;

    private static ExtensionPlugin[] plugins;

    public static ExtensionPlugin[] getPlugins() {
        return plugins;
    }

    public static Map<Class<?>, Entity.Extension> getExtensionMap() {
        return extensionMap;
    }

    public static void registerExtensionMap() throws Exception {
        Map<Class<?>, Entity.Extension> extensionMap1 = DomParser.parse();
        Map<Class<?>, Entity.Extension> extensionMap2 = AnnotationParser.parse();
        extensionMap = mergeExtensionMap(extensionMap1, extensionMap2);
    }

    public static void registerExtensionPlugins() {
        try {
            List<ExtensionPlugin> _plugins = new LinkedList<>();
            for (ExtensionPlugin plugin : ServiceLoader.load(ExtensionPlugin.class, ExtensionPlugin.class.getClassLoader())) {
                log.info("loaded [ExtensionPlugin]: {}", plugin);
                _plugins.add(plugin);
            }
            plugins = _plugins.toArray(new ExtensionPlugin[0]);
        } catch (Throwable t) {
            log.error("loading [ExtensionPlugin] error.", t);
            throw Throwables.propagate(t);
        }
    }

    private static Map<Class<?>, Entity.Extension> mergeExtensionMap(Map<Class<?>, Entity.Extension> extensionMap1,
                                                                     Map<Class<?>, Entity.Extension> extensionMap2) {

        for (Map.Entry<Class<?>, Entity.Extension> entry : extensionMap2.entrySet()) {
            Class<?> extension = entry.getKey();
            Entity.Extension extensionEntity = entry.getValue();
            Entity.Extension originExtensionEntity = extensionMap1.get(extension);
            if (originExtensionEntity == null) {
                extensionMap1.put(extension, extensionEntity);
            } else {
                doMerge(originExtensionEntity, extensionEntity);
            }
        }

        return ImmutableMap.copyOf(extensionMap1);
    }

    private static void doMerge(Entity.Extension originExtensionEntity, Entity.Extension extensionEntity) {
        for (Map.Entry<String, List<Entity.Business>> entry : extensionEntity.businessMap.entrySet()) {
            String code = entry.getKey();
            List<Entity.Business> businesses = entry.getValue();
            List<Entity.Business> resultBusinesses = originExtensionEntity.businessMap.computeIfAbsent(code, _K -> new ArrayList<>());
            resultBusinesses.addAll(businesses);
            resultBusinesses.sort(Comparator.comparingInt(biz -> biz.priority));
        }
    }

    protected static SpiImpls getSpiImpls(String group, Class<?> spi, Object... args) {
        ExtensionRouter router = ExtensionContext.getExtensionRouter(group);
        if (router == null) {
            throw new ExtensionException("[ExtensionRouter] can't be null: please invoke Extension.parse(...) before.");
        }


        SpiImpls impls = router.route(group, spi, args);
        if (CollectionUtils.isEmpty(impls)) {
            throw new ExtensionException(String.format("[BizRouter] '%s' [Spi] '%s' route return empty impls !", router, spi));
        }
        return impls;
    }

    public static SpiImpls getBusinessSpiImpls(Class<?> spi, String code) {
        Entity.Extension extension = extensionMap.get(spi);
        if (extension == null) {
            throw new ExtensionException(String.format("ExtensionSpi:[%s] not found.", spi.getName()));
        }

        return extension.CODE2IMPL_CACHE.computeIfAbsent(code, _K -> {
            List<Entity.Business> business = extension.businessMap.get(code);
            if (CollectionUtils.isEmpty(business)) {
                return new SpiImpls(Collections.singletonList(new SpiImpls.SpiImpl("base", extension.base)));
            } else {
                return new SpiImpls(business.stream().map(ExtensionManager::makeImpl).collect(toList()));
            }
        });
    }

    private static SpiImpls.SpiImpl makeImpl(Entity.Business entity) {
        if (entity.instance != null) {
            return new SpiImpls.SpiImpl(entity.type, entity.instance);
        }

        // tips: 懒加载的具体实现
        try {
            if (entity.hsf != null) {
                entity.instance = HsfServiceFactory.getHsfService(entity.hsf);
            }
            if (entity.bean != null) {
                entity.instance = SpringBeanFactory.getSpringBean(entity.bean);
            }
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }

        Preconditions.checkState(entity.instance != null);

        return new SpiImpls.SpiImpl(entity.type, entity.instance);
    }
}
