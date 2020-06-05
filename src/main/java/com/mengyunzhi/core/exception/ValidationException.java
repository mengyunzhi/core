package com.mengyunzhi.core.exception;

/**
 *
 * 较验错误
 * @author panjie on 2018/1/16
 */
public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = -4460002290487865340L;

    public ValidationException(final String message) {
        super(message);
    }
}
