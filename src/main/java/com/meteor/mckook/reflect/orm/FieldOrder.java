package com.meteor.mckook.reflect.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 因为没找到轻量级的ORM框架，这个包下的代码实现了一些实体映射的逻辑
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldOrder {
    int value();
}
