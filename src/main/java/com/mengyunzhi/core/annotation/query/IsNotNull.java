package com.mengyunzhi.core.annotation.query;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNotNull {
    // 以应的查询字段名称
    String name() default "";
}
