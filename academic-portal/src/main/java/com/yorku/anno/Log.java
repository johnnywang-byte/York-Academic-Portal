package com.yorku.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom Operation Log Annotation
 * 自定义操作日志注解
 * * Description: Used to mark methods that require operation logging.
 * 描述：用于标记需要记录操作日志的方法。
 */
@Target(ElementType.METHOD) // Scope: Method level (作用范围：仅限方法)
@Retention(RetentionPolicy.RUNTIME) // Lifecycle: Available at runtime via reflection (生命周期：运行时有效，可通过反射获取)
public @interface Log {
    // Currently serves as a marker interface with no attributes.
    // 当前仅作为标记接口使用，无额外属性。
}