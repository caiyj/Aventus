package com.alibaba.aventus.test.extension;

import org.springframework.util.ClassUtils;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/11/2 19:46.
 */
public class Test {

    public static interface A {

    }

    public static class B implements A {

    }

    public static class C extends B {

    }

    public static void main(String[] args) {
        final Class<?>[] interfaces = C.class.getInterfaces();
        final Class<?>[] allInterfaces = ClassUtils.getAllInterfaces(new C());

        System.out.println(interfaces);
        System.out.println(allInterfaces);
    }
}
