package com.alibaba.aventus.extension.domain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 18:08.
 */
public class Entity {

    public static class Extension {

        public boolean proxy;

        @Nonnull
        public final String clazz;

        // 基础实现是一定不支持懒加载的
        @Nonnull
        public final Object base;

        @Nonnull
        public final Map<String, List<Business>> businessMap; // bizCode -> business

        @Nonnull
        public final ConcurrentMap<String, SpiImpls> CODE2IMPL_CACHE = new ConcurrentHashMap<>();

        public Extension(@Nonnull String clazz, @Nonnull Object base, @Nonnull Map<String, List<Business>> businessMap) {
            this.clazz = clazz;
            this.base = base;
            this.businessMap = businessMap;
        }
    }

    public static class Business {

        @Nonnull
        public final String code;

        @Nonnull
        public final String type;

        @Nonnull
        public int priority;

        @Nullable
        public Tag.Hsf hsf = null;

        @Nullable
        public Tag.Bean bean = null;

        @Nullable
        public volatile Object instance = null;

        private Business(@Nonnull String code, @Nonnull String type) {
            this.code = code;
            this.type = type;
        }

        public static Business newHsfInstance(@Nonnull String code, @Nonnull String type, int priority, @Nonnull Tag.Hsf hsf, @Nullable Object instance) {
            Business business = new Business(code, type);
            business.hsf = hsf;
            business.instance = instance;
            business.priority = priority;
            return business;
        }

        public static Business newBeanInstance(@Nonnull String code, @Nonnull String type, int priority, @Nullable Tag.Bean bean, @Nullable Object instance) {
            Business business = new Business(code, type);
            business.bean = bean;
            business.instance = instance;
            business.priority = priority;
            return business;
        }
    }
}
