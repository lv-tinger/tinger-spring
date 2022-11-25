package org.tinger.spring.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
@Builder
public class SpringJsonInvoker {
    private Integer id;
    private Object bean;
    private Class<?> beanType;
    private Method method;
    private Class<?>[] parameters;

    public Object invoke(Object... parameters) {
        try {
            return method.invoke(bean, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}