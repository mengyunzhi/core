package com.mengyunzhi.core.annotation.query;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 完全等于
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EqualTo {
    @AliasFor("name")
    String value() default "";
    // 以应的查询字段名称
    @AliasFor("value")
    String name() default "";
}
