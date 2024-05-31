package com.alibaba.aventus.flow;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/4/10 16:01.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Flow<T extends FlowContext, R> {

    protected static final ThreadLocal<Map<String, Meta>> metas = ThreadLocal.withInitial(HashMap::new);

    private String name;

    private FlowNode[] nodes;

    public R execute(T context) throws Throwable {
        context.name = name;
        metas.get().put(name, new Meta(name, nodes));
        try {
            return (R) nodes[0].next(context);
        } finally {
            metas.get().remove(name);
        }
    }

    public void clear() {
        metas.remove();
    }
}
