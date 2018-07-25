package com.mengyunzhi.core.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 范围查询 -- 小于等于
 * @author panjie
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LessThanOrEqualTo {
    // 以应的查询字段
    String name();
}