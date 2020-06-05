package com.mengyunzhi.core.exception;

/**
 * 无此操作权限
 *
 * @author panjie on 2018/1/16
 */
public class AccessDeniedException extends RuntimeException {
    private static final long serialVersionUID = -2762353370211004942L;

    public AccessDeniedException(final String message) {
        super(message);
    }
}
