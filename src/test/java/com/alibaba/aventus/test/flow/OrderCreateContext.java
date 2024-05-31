package com.alibaba.aventus.test.flow;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jifang.zjf@alibaba-inc.com (FeiQing)
 * @version 1.0
 * @since 2024/5/31 16:23.
 */
@Data
public class OrderCreateContext implements Serializable {

    private static final long serialVersionUID = -3284338197634811827L;

    private long itemId;

}
