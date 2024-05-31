package com.alibaba.aventus.flow;

import static com.alibaba.aventus.logging.AventusLogging.flowLogger;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/4/10 15:47.
 */
public abstract class FlowNode<T extends FlowContext, R> {

    public abstract R execute(T context) throws Throwable;

    public String desc(T context) {
        return this.getClass().getName();
    }

    protected R next(T context) throws Throwable {
        Meta meta = Flow.metas.get().get(context.name);
        // 已经到达终点
        if (!(++meta.idx < meta.nodes.length)) {
            throw new IllegalStateException("tasks has already reach end !");
        }

        FlowNode node = meta.nodes[meta.idx];
        String desc = String.format("%s[%s/%s]('%s')", meta.name, meta.idx + 1, meta.nodes.length, node.desc(context));

        try {
            meta.setAttribute(desc, System.currentTimeMillis());
            return (R) node.execute(context);
        } catch (Throwable t) {
            flowLogger.error("[NODE] {} occur exception.", desc, t);
            throw t;
        } finally {
            long total = System.currentTimeMillis() - (long) meta.removeAttribute(desc);
            if (meta.trace && flowLogger.isTraceEnabled()) {
                flowLogger.trace("[NODE] {} cost '{}', total '{}'.", desc, total - meta.cost, total);
            }
            meta.cost = total;
        }
    }

}
