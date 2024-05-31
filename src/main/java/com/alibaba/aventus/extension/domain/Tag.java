package com.alibaba.aventus.extension.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/11 18:08.
 */
public class Tag {

    public static class Extension implements Serializable {

        private static final long serialVersionUID = 6557095413659207121L;

        public String clazz;

        public String base;

        public boolean proxy;

        public String desc;

        public List<Business> businessList;

        public Extension(String clazz, String base) {
            this.clazz = clazz;
            this.base = base;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Extension extension = (Extension) o;

            return clazz.equals(extension.clazz);
        }

        @Override
        public int hashCode() {
            return clazz.hashCode();
        }
    }

    public static class Business implements Serializable {

        private static final long serialVersionUID = -253145366338161711L;

        public final String code;

        public final String type;

        public String desc;

        public Bean bean;

        public Hsf hsf;

        public int priority = 0;

        public Business(String code, String type) {
            this.code = code;
            this.type = type;
        }
    }

    public static class Bean implements Serializable {

        private static final long serialVersionUID = -1946088618528756952L;

        public String name;

        public boolean lazy = false;

        public Bean(String name) {
            this.name = name;
        }
    }

    public static class Hsf implements Serializable {

        private static final long serialVersionUID = -6757315625134300013L;

        public String service;

        public String version;

        public String group = "HSF";

        public Integer timeout;

        public boolean lazy = false;

        public Hsf(String service, String version) {
            this.service = service;
            this.version = version;
        }
    }
}
