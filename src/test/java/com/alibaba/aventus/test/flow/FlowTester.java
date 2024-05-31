package com.alibaba.aventus.test.flow;

import com.alibaba.aventus.flow.Flow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 11:56.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring-*.xml")
public class FlowTester {

    @Resource
    private Flow<OrderCreateContext, Long> flow;

    @Test
    public void test() throws Throwable {
        OrderCreateContext input = new OrderCreateContext();
        input.setItemId(new Random().nextInt());
        long bizOrderId = flow.execute(input);
        System.out.println("bizOrderId = " + bizOrderId);
    }


//    @Bean
//    public Flow<FlowContext, Void> ttFlow(@Autowired TestNode node) {
//        Flow<FlowContext, Void> flow = new Flow<>();
//        flow.setName("fuck");
//        flow.setNodes(new FlowNode[]{
//                node,
//                node,
//                node
//        });
//
//        return flow;
//    }
}
