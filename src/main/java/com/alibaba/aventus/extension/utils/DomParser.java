package com.alibaba.aventus.extension.utils;

import com.alibaba.aventus.extension.domain.Entity;
import com.alibaba.aventus.extension.domain.Tag;
import com.alibaba.aventus.extension.exception.ExtensionException;
import com.alibaba.aventus.extension.factory.HsfServiceFactory;
import com.alibaba.aventus.extension.factory.SpringBeanFactory;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 19:38.
 */
@Slf4j
public class DomParser {

    private static final SAXReader saxReader = new SAXReader();

    private static final String AVENTURS_XML_CONFIG_LOCATION = "classpath*:aventus-extension-*.xml";

    public static Map<Class<?>, Entity.Extension> parse() throws Exception {
        Set<Tag.Extension> extensions = DomParser.loadingConfigFiles(AVENTURS_XML_CONFIG_LOCATION);

        return toExtensionMap(extensions);
    }

    public static Set<Tag.Extension> loadingConfigFiles(String configFileLocation) throws Exception {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(configFileLocation);
        Set<Tag.Extension> allExtensions = new HashSet<>(resources.length);

        for (Resource resource : resources) {
            String file = resource.getFilename();
            List<Tag.Extension> extensions = loadingExtensions(file, saxReader.read(resource.getInputStream()).getRootElement());

            for (Tag.Extension extension : extensions) {
                if (!allExtensions.add(extension)) {
                    throw new ExtensionException(String.format("Extension:[%s] is duplicated in file:[%s]", extension.clazz, file));
                }

                log.info("loaded Extension:[{}/{}] business:[{}] from config file:[{}].", extension.clazz, extension.desc, extension.businessList.stream().map(business -> business.code).collect(Collectors.joining(" | ")), file);
            }
        }

        return allExtensions;
    }

    private static List<Tag.Extension> loadingExtensions(String file, Element document) {

        List<Tag.Extension> extensions = new LinkedList<>();
        for (Iterator<Element> iterator = document.elementIterator(); iterator.hasNext(); ) {
            extensions.add(loadingExtension(file, iterator.next()));
        }

        return extensions;
    }

    private static Tag.Extension loadingExtension(String file, Element element) {
        String clazz = getAttrValNoneNull(element, file, null, "<Extension/>", "class");
        String base = getAttrValNoneNull(element, file, null, "<Extension/>", "base");

        Tag.Extension extension = new Tag.Extension(clazz, base);
        extension.businessList = new LinkedList<>();
        for (Iterator<Element> iterator = element.elementIterator(); iterator.hasNext(); ) {
            extension.businessList.add(loadingBusiness(file, clazz, iterator.next()));
        }
        extension.desc = element.attributeValue("desc");
        extension.proxy = BooleanUtils.toBoolean(element.attributeValue("proxy"));

        return extension;
    }

    private static Tag.Business loadingBusiness(String file, String path, Element element) {
        String code = getAttrValNoneNull(element, file, path, "<Business/>", "code");
        String type = getAttrValNoneNull(element, file, path, "<Business/>", "type");

        Tag.Business business = new Tag.Business(code, type);
        business.desc = element.attributeValue("desc");
        ofNullable(element.attributeValue("priority")).map(Integer::valueOf).ifPresent(priority -> business.priority = priority);

        if (StringUtils.equals("bean", type)) {
            business.bean = loadingBean(file, path + "#" + code, element.element("bean"));
        } else if (StringUtils.equals("hsf", type)) {
            business.hsf = loadingHsf(file, path + "#" + code, code, element.element("hsf"));
        } else {
            throw new ExtensionException(String.format("path:[%s] business code: %s 's type:[%s] is not support in file:[%s].", path, code, type, file));
        }

        return business;
    }

    private static Tag.Bean loadingBean(String file, String path, Element element) {
        String tag = "<bean/>";
        if (element == null) {
            throw new ExtensionException(String.format("%s's tag %s definition can not be null in file:[%s].", path, tag, file));
        }

        String name = getAttrValNoneNull(element, file, path, tag, "name");
        Tag.Bean bean = new Tag.Bean(name);
        ofNullable(element.attributeValue("lazy")).map(Boolean::valueOf).ifPresent(lazy -> bean.lazy = lazy);
        return bean;
    }

    private static Tag.Hsf loadingHsf(String file, String path, String codes, Element element) {
        String tag = "<hsf/>";
        if (element == null) {
            throw new ExtensionException(String.format("%s's tag %s definition can not be null in file:[%s].", path, tag, file));
        }

        String service = getAttrValNoneNull(element, file, path, tag, "service");
        String version = getAttrValNoneNull(element, file, path, tag, "version");
//        if (Splitter.on(",").trimResults().omitEmptyStrings().splitToList(codes).stream().noneMatch(version::endsWith)) {
//            throw new WaterException(String.format("service:[%s] version:[%s] error in file:[%s].", service, version, file));
//        }

        Tag.Hsf hsf = new Tag.Hsf(service, version);
        ofNullable(element.attributeValue("group")).ifPresent(group -> hsf.group = group);
        ofNullable(element.attributeValue("timeout")).map(Integer::valueOf).ifPresent(timeout -> hsf.timeout = timeout);
        ofNullable(element.attributeValue("lazy")).map(Boolean::valueOf).ifPresent(lazy -> hsf.lazy = lazy);

        return hsf;
    }

    private static @Nonnull String getAttrValNoneNull(Element element, String file, String path, String tag, String attr) {
        String value = element.attributeValue(attr);
        if (Strings.isNullOrEmpty(value)) {
            if (Strings.isNullOrEmpty(path)) {
                throw new ExtensionException(String.format("tag:%s's attr '%s' can not be empty in file:[%s].", tag, attr, file));
            } else {
                throw new ExtensionException(String.format("path:[%s] tag:%s's attr '%s' can not be empty in file:[%s].", path, tag, attr, file));
            }
        }

        return value;
    }

    public static Map<Class<?>, Entity.Extension> toExtensionMap(Collection<Tag.Extension> extensionTags) throws Exception {
        Map<Class<?>, Entity.Extension> extensionMap = new HashMap<>(extensionTags.size());
        for (Tag.Extension extensionTag : extensionTags) {
            Object base = SpringBeanFactory.getSpringBean(new Tag.Bean(extensionTag.base));

            Map<String, List<Entity.Business>> businessMap = toBusinessMap(extensionTag.businessList);

            Entity.Extension extension = new Entity.Extension(extensionTag.clazz, base, businessMap);
            extension.proxy = extensionTag.proxy;

            extensionMap.put(Class.forName(extensionTag.clazz), extension);
        }


        return extensionMap;
    }


    private static Map<String, List<Entity.Business>> toBusinessMap(List<Tag.Business> tags) throws Exception {
        // tips: 此处要将code分割后重新排列
        Map<String, List<Tag.Business>> code2tags = new HashMap<>();
        for (Tag.Business tag : tags) {
            for (String code : Splitter.on(",").trimResults().omitEmptyStrings().split(tag.code)) {
                code2tags.computeIfAbsent(code, _k -> new ArrayList<>()).add(tag);
            }
        }

        Map<String, List<Entity.Business>> code2businessMap = new HashMap<>(code2tags.size());
        for (Map.Entry<String, List<Tag.Business>> entry : code2tags.entrySet()) {
            // tips: 基于优先级重新排序
            // -> 业务优先级在code打散之后就可以进行重新排序了
            // -> 但是路由的优先级需要match后再进行重新排序
            entry.getValue().sort(Comparator.comparing(bizTag -> bizTag.priority));

            List<Entity.Business> businessList = new ArrayList<>(entry.getValue().size());
            for (Tag.Business tag : entry.getValue()) {
                businessList.add(toBusinessEntity(tag));
            }

            code2businessMap.put(entry.getKey(), businessList);
        }

        return code2businessMap;
    }

    private static Entity.Business toBusinessEntity(Tag.Business tag) throws Exception {
        if (tag.bean != null) {
            return Entity.Business.newBeanInstance(tag.code, tag.type, tag.priority, tag.bean, getSpringBean(tag.bean));
        }

        if (tag.hsf != null) {
            return Entity.Business.newHsfInstance(tag.code, tag.type, tag.priority, tag.hsf, getHsfService(tag.hsf));
        }

        throw new ExtensionException(String.format("Business:[%s] <bean/> and <hsf/> definition all empty.", tag.code));
    }

    private static Object getSpringBean(Tag.Bean bean) {
//        if (SystemConfig.isGlobalCloseLazyLoading()) {
//            return SpringBeanFactory.getSpringBean(bean);
//        }
//
//        if (SystemConfig.isGlobalOpenLazyLoading()) {
//            return null;
//        }

        if (!bean.lazy) {
            return SpringBeanFactory.getSpringBean(bean);
        }

        return null;
    }

    private static Object getHsfService(Tag.Hsf hsf) throws Exception {
//        if (SystemConfig.isGlobalCloseLazyLoading()) {
//            return HsfServiceFactory.getHsfService(hsf);
//        }
//
//        if (SystemConfig.isGlobalOpenLazyLoading()) {
//            return null;
//        }

        if (!hsf.lazy) {
            return HsfServiceFactory.getHsfService(hsf);
        }

        return null;
    }
}
