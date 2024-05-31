package com.alibaba.aventus.test.extension;

import com.alibaba.aventus.extension.Extension;
import com.alibaba.aventus.extension.ExtensionParser;
import com.alibaba.aventus.extension.reducer.Reducers;
import com.alibaba.aventus.extension.spring.ExtensionSpringBean;
import com.alibaba.aventus.test.extension.domain.OrderCreateParam;
import com.alibaba.aventus.test.extension.spi.OrderCreateSpi;
import com.alibaba.aventus.test.extension.spi.SuperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;
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

    @Resource
    private OrderCreateSpi orderCreateSpi;

    @Test
    public void test() throws Exception {

        // 业务身份解析
        Extension.parse(DemoExtensionParser.class, new String[]{"base"});


        OrderCreateParam param = new OrderCreateParam();

        // 扩展点调用
        // boolean isUseRedPacket = Extension.execute(OrderCreateSpi.class, spi -> spi.isUseRedPacked(param));
        boolean isUseRedPacket = orderCreateSpi.isUseRedPacked(param);
        System.out.println(isUseRedPacket);

        // List<Map<String, Object>> attributes = Extension.execute(OrderCreateSpi.class, spi -> spi.getCustomOrderAttributes(param), Reducers.collect());
        Map<String, Object> attributes = orderCreateSpi.getCustomOrderAttributes(param);
        System.out.println(attributes);

        // 上下文清理
        Extension.clear();
    }

    @Bean
    public ExtensionSpringBean<OrderCreateSpi> orderCreateSpi() {
        ExtensionSpringBean<OrderCreateSpi> springBean = new ExtensionSpringBean<>();
        springBean.setExtension(OrderCreateSpi.class);
        springBean.setReducer(Reducers.firstOf());
        return springBean;
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
