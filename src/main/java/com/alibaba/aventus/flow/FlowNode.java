package com.alibaba.aventus.flow;

import static com.alibaba.aventus.flow.Flow.metas;
import static com.alibaba.aventus.logging.AventusLogging.flowLogger;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/4/10 15:47.
 */
public abstract class FlowNode<T extends FlowContext, R> {

    protected abstract R execute(T context) throws Throwable;

    protected String desc(T context) {
        return this.getClass().getName();
    }

    protected final R next(T context) throws Throwable {
        Flow.Meta meta = metas.get().get(context.flowName);
        // 已经到达终点
        if (!(++meta.idx < meta.nodes.length)) {
            throw new IllegalStateException("[FLOW] has already reach end !");
        }

        FlowNode node = meta.nodes[meta.idx];
        String desc = String.format("%s[%s/%s]('%s')", meta.name, meta.idx + 1, meta.nodes.length, node.desc(context));

        try {
            meta.setAttribute(desc, System.currentTimeMillis());
            return (R) node.execute(context);
        } catch (Throwable t) {
            flowLogger.error("[FLOW:{}] {} occur exception.", desc, t);
            throw t;
        } finally {
            long total = System.currentTimeMillis() - (long) meta.removeAttribute(desc);
            if (flowLogger.isTraceEnabled()) {
                flowLogger.trace("[FLOW:{}] {} cost '{}', total '{}'.", desc, total - meta.cost, total);
            }
            meta.cost = total;
        }
    }
}
