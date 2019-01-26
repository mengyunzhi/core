package com.mengyunzhi.core.exception;

/**
 * 实体未找到
 * panjie
 */
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
