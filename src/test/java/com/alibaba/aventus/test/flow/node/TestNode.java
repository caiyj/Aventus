package com.alibaba.aventus.test.flow.node;

import com.alibaba.aventus.flow.FlowContext;
import com.alibaba.aventus.flow.FlowNode;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/4/10 15:59.
 */
public class TestNode extends FlowNode<FlowContext, Void> {

    @Override
    public Void execute(FlowContext context) throws Throwable {

        return next(context);
    }
}
