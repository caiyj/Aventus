package com.alibaba.aventus.test.extension;

import com.alibaba.aventus.extension.Extension;
import com.alibaba.aventus.extension.ExtensionParser;
import com.alibaba.aventus.test.extension.spi.SuperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Function;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/8/16 14:10.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring-*.xml")
public class ExtensionTester {

    @Test
    public void test_交叉运行() throws Exception {
//        ExtensionStaticStarter.start("classpath:spring-*.xml");

        System.out.println("code: " + Extension.parse(DemoExtensionParser.class, new String[]{"base"}));
        String result = Extension.execute(SuperService.class, service -> service.handle1("base"));
        System.out.println(result);

        // 二次解析
        System.out.println("code: " + Extension.parse(SubGroupExtensionParser.class, new String[]{"yhb"}));
        result = (String) Extension.execute("SUB-GROUP", Function.class, function -> function.apply("yhb"));
        System.out.println(result);

        result = Extension.execute(SuperService.class, service -> service.handle1("base"));
        System.out.println(result);

        Extension.clear();
    }

    public void main() throws Exception {

        System.out.println("code: " + Extension.parse(DemoExtensionParser.class, new String[]{"base"}));
        String result = Extension.execute(SuperService.class, service -> service.handle1("base"));
        System.out.println(result);

        result = Extension.execute(SuperService.class, service -> service.handle1("base"));
        System.out.println(result);

        System.out.println("code: " + Extension.parse(DemoExtensionParser.class, new String[]{"yhb"}));
        result = Extension.execute(SuperService.class, service -> service.handle2("yhb"));
        System.out.println(result);

        System.out.println("code: " + Extension.parse(DemoExtensionParser.class, new String[]{"hs"}));
        result = Extension.execute(SuperService.class, service -> service.handle2("hs"));
        System.out.println(result);

        Extension.clear();
    }

    public static class DemoExtensionParser implements ExtensionParser<String[]> {

        @Override
        public String parseExtensionCode(String[] args) {
            return args[0];
        }
    }

    public static class SubGroupExtensionParser implements ExtensionParser<String[]> {

        @Override
        public String parseExtensionGroup(String[] strings) {
            return "SUB-GROUP";
        }

        @Override
        public String parseExtensionCode(String[] args) {
            return args[0];
        }
    }


}
