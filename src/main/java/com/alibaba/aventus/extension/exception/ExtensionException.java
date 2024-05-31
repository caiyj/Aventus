package com.alibaba.aventus.extension.exception;

/**
 * @author qingfei
 * @date 2022/05/16
 */
public class ExtensionException extends RuntimeException {

    private static final long serialVersionUID = -2060948238287068976L;

    public ExtensionException() {
    }

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtensionException(Throwable cause) {
        super(cause);
    }

    public ExtensionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
