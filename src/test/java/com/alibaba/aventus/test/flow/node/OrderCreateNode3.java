package com.alibaba.aventus.test.flow.node;

import com.alibaba.aventus.flow.FlowNode;
import com.alibaba.aventus.test.flow.OrderCreateContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 16:41.
 */
@Slf4j
public class OrderCreateNode3 extends FlowNode<OrderCreateContext, Long> {
    @Override
    protected Long execute(OrderCreateContext context) throws Throwable {

        log.info("OrderCreateNode3...");

        return new Random().nextLong();
    }
}
