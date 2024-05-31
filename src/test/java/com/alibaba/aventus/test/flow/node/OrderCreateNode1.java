package com.alibaba.aventus.test.flow.node;

import com.alibaba.aventus.flow.FlowNode;
import com.alibaba.aventus.test.flow.OrderCreateContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 16:22.
 */
@Slf4j
public class OrderCreateNode1 extends FlowNode<OrderCreateContext, Long> {

    @Override
    protected Long execute(OrderCreateContext context) throws Throwable {
        log.info("itemId = {}", context.getItemId());

        return next();
    }
}
