package com.mengyunzhi.core.exception;

/**
 * 关联删除异常
 *
 * @author panjie
 */
public class AssociateDeleteException extends RuntimeException {
    private static final long serialVersionUID = -26281546072902699L;

    public AssociateDeleteException(final String message) {
        super(message);
    }
}