package com.alibaba.aventus.test.extension;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/11/8 15:20.
 */
public class ProxyTest {


    public static class A {

        public String s(String a) {
            System.out.println("hh:" + a);
            return a;
        }
    }

    private static final ConcurrentMap<Object, Object> target2proxy = new ConcurrentHashMap<>();


    public static <T> T proxy(T target) {
        return (T) target2proxy.computeIfAbsent(target, _K -> {
            Enhancer enhancer = new Enhancer();
//            enhancer.setClassLoader(target.getClass().getClassLoader());
            enhancer.setSuperclass(target.getClass());
            enhancer.setCallback(new MethodInterceptor() {

                {
                    if (true) {
                        throw new RuntimeException("jj");
                    }
                }

                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    System.out.println("start proxy ");
                    try {
                        return method.invoke(target, objects);
                    } finally {
                        System.out.println("end proxy");
                    }
                }
            });
            return enhancer.create();
        });
    }

    public static void main(String[] args) {
        A target = new A();

        for (int i = 0; i < 100; ++i) {

            final String feiqing = proxy(target).s("feiqing");

            System.out.println(feiqing);
        }
    }
}
