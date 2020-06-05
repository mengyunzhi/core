package com.mengyunzhi.core.exception;

/**
 * 实体未找到
 * @author panjie
 */
public class ObjectNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -1059213824557212829L;

    public ObjectNotFoundException(final String message) {
        super(message);
    }
}
