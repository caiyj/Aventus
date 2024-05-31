package com.alibaba.aventus.flow;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/4/10 16:01.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Flow<Context, Output> {

    protected static final ThreadLocal<Map<String, Meta>> metas = ThreadLocal.withInitial(HashMap::new);

    @Getter
    private final String name;

    private final FlowNode[] nodes;

    public Flow(String name, FlowNode[] nodes) {
        this.name = name;
        this.nodes = nodes;

        for (FlowNode node : this.nodes) {
            node.name = name;
        }
    }

    public Output execute(Context context) throws Throwable {
        metas.get().put(name, new Meta(nodes, context));
        try {
            return (Output) nodes[0].next();
        } finally {
            metas.get().remove(name);
        }
    }

    public void clear() {
        metas.remove();
    }

    protected static class Meta {

        protected final FlowNode[] nodes;

        protected final Object context;

        protected int idx = -1;

        protected long cost = 0;

        private Map<String, Object> attributes;

        public Meta(FlowNode[] nodes, Object input) {
            this.nodes = nodes;
            this.context = input;
        }

        public Map<String, Object> getAttributes() {
            if (this.attributes == null) {
                this.attributes = new HashMap<>();
            }
            return this.attributes;
        }

        public void setAttribute(String key, Object value) {
            getAttributes().put(key, value);
        }

        public Object getAttribute(String key) {
            return getAttributes().get(key);
        }

        public Object removeAttribute(String key) {
            return getAttributes().remove(key);
        }
    }
}
