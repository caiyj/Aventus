package com.alibaba.aventus.flow;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/4/10 16:22.
 */
@SuppressWarnings("rawtypes")
class Meta {

    protected String name;

    protected FlowNode[] nodes;

    public Meta(String name, FlowNode[] nodes) {
        this.name = name;
        this.nodes = nodes;
    }

    protected int idx = -1;

    protected long cost = 0;

    protected boolean trace = true;

    private Map<String, Object> attributes;

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
