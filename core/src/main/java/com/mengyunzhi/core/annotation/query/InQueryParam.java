package com.mengyunzhi.core.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 范围查询
 * @author zhangxishuo on 2018/7/16
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InQueryParam {
    // in查询的字段
    String name();
}
