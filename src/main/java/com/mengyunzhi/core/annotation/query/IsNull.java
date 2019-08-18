package com.mengyunzhi.core.annotation.query;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当注解的字段值不为null且不为false时，生效
 *  @IsNull("targetFiled")
 *  private Boolean isNull = true;
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNull {
    // 以应的查询字段名称
    String name() default "";
}
