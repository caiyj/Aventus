package com.alibaba.aventus.flow;

import lombok.Getter;
import lombok.Setter;

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

    @Getter
    @Setter
    private String name;

    @Setter
    private FlowNode[] nodes;

    public R execute(T context) throws Throwable {
        context.flowName = name;
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

    protected static class Meta {

        protected final String name;

        protected final FlowNode[] nodes;

        protected int idx = -1;

        protected long cost = 0;

        private Map<String, Object> attributes;

        public Meta(String name, FlowNode[] nodes) {
            this.name = name;
            this.nodes = nodes;
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
