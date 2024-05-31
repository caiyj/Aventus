package com.alibaba.aventus.test.extension.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/24 16:49.
 */
public class MyAspect {

    public Object around(ProceedingJoinPoint pjp) {
        Object returnValue;
        try {
            //获取方法执行所需的参数
            Object[] args = pjp.getArgs();

            System.out.println("前置通知。。。" + ((MethodSignature) pjp.getSignature()).getMethod().getName());

            //调用切入点方法
            returnValue = pjp.proceed(args);

            System.out.println("后置通知。。。");


            return returnValue;

        } catch (Throwable t) {
            System.out.println("异常通知。。。");
            throw new RuntimeException(t);
        } finally {
            System.out.println("最终通知。。。");
        }
    }
}
