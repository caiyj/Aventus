package com.alibaba.aventus.extension.domain;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2023/11/1 14:53.
 */
public class SpiImpls extends ArrayList<SpiImpls.SpiImpl> {

    private static final long serialVersionUID = -7467748386118196504L;


    public SpiImpls() {
    }

    public SpiImpls(int initialCapacity) {
        super(initialCapacity);
    }

    public SpiImpls(Collection<? extends SpiImpl> c) {
        super(c);
    }

    public static class SpiImpl {

        @Nonnull
        public final String type;

        @Nonnull
        public final Object instance;

        public SpiImpl(@Nonnull String type, @Nonnull Object instance) {
            this.type = type;
            this.instance = instance;
        }
    }
}
