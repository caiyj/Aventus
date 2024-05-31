package com.alibaba.aventus.flow;

import static com.alibaba.aventus.flow.Flow.metas;
import static com.alibaba.aventus.logging.AventusLogging.flowLogger;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/4/10 15:47.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class FlowNode<Context, Output> {

    protected String name;

    protected abstract Output execute(Context context) throws Throwable;

    protected Flow.Meta getMeta() {
        return metas.get().get(name);
    }

    protected String desc(Context context) {
        return this.getClass().getName();
    }

    protected final Output next() throws Throwable {
        Flow.Meta meta = metas.get().get(name);
        // 已经到达终点
        if (!(++meta.idx < meta.nodes.length)) {
            throw new IllegalStateException("[FLOW] '" + name + "' has already reach end !");
        }

        FlowNode node = meta.nodes[meta.idx];
        String desc = String.format("'%s:%d:%s'", name, meta.idx, node.desc(meta.context));

        try {
            meta.setAttribute(desc, System.currentTimeMillis());
            return (Output) node.execute(meta.context);
        } catch (Throwable t) {
            flowLogger.error("[FLOW] {} throw exception.", desc, t);
            throw t;
        } finally {
            long total = System.currentTimeMillis() - (long) meta.removeAttribute(desc);
            if (flowLogger.isTraceEnabled()) {
                flowLogger.trace("[FLOW] {} cost {}/{}.", desc, total - meta.cost, total);
            }
            meta.cost = total;
        }
    }
}
