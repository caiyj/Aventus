package com.alibaba.aventus.test.flow;

import com.alibaba.aventus.flow.Flow;
import com.alibaba.aventus.flow.FlowContext;
import com.alibaba.aventus.flow.FlowNode;
import com.alibaba.aventus.test.flow.node.TestNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 11:56.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring-*.xml")
public class FlowTester {

    @Autowired
    private Flow<FlowContext, Void> testFlow;

    @Test
    public void test() throws Throwable {
        final FlowContext flowContext = new FlowContext();
        final Void execute = testFlow.execute(flowContext);
        System.out.println(execute);
        testFlow.clear();
    }

    @Bean
    public Flow<FlowContext, Void> ttFlow(@Autowired TestNode node) {
        Flow<FlowContext, Void> flow = new Flow<>();
        flow.setName("fuck");
        flow.setNodes(new FlowNode[]{
                node,
                node,
                node
        });

        return flow;
    }
}
