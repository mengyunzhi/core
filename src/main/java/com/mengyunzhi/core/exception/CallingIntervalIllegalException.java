package com.mengyunzhi.core.exception;

/**
 * 调用间隔非法异常
 * @author panjie
 */
public class CallingIntervalIllegalException extends RuntimeException {
    private static final long serialVersionUID = 4053838713399923245L;

    public CallingIntervalIllegalException(final String message) {
        super(message);
    }
}
