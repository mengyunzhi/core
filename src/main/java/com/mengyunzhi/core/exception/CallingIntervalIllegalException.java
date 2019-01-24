package com.mengyunzhi.core.exception;

/**
 * @author panjie
 * 调用间隔非法异常
 */
public class CallingIntervalIllegalException extends RuntimeException {
    public CallingIntervalIllegalException(String message) {
        super(message);
    }
}
