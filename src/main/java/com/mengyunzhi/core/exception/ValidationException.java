package com.mengyunzhi.core.exception;

/**
 * @author panjie on 2018/1/16
 * 较验错误
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
