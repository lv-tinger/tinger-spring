package org.tinger.spring.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.tinger.common.utils.HashUtils;
import org.tinger.common.utils.MethodUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

@Component
public class TingerJsonInvokerBeanPostProcessor implements BeanPostProcessor {

    @Resource
    private TingerSpringController tingerSpringController;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Method[] methods = MethodUtils.getDeclaredMethod(beanClass);
        for (Method method : methods) {
            if (method == null) {
                continue;
            }
            int modifiers = method.getModifiers();
            if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers) || Modifier.isAbstract(modifiers)) {
                continue;
            }

            JsonInvoker jsonInvoker = method.getAnnotation(JsonInvoker.class);
            if (jsonInvoker == null) {
                continue;
            }

            int id = calculateId(beanClass, method);
            SpringJsonInvoker invoker = SpringJsonInvoker.builder()
                    .id(id)
                    .bean(bean)
                    .beanType(beanClass)
                    .method(method)
                    .parameters(method.getParameterTypes())
                    .build();

            tingerSpringController.register(invoker);
        }

        return bean;
    }

    private int calculateId(Class<?> beanClass, Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(beanClass.getName());
        builder.append(method.getName());
        builder.append("(");
        Parameter[] parameters = method.getParameters();
        int i = 0;
        for (Parameter parameter : parameters) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameter.getType().getSimpleName());
            i += 1;
        }
        builder.append(")");

        return HashUtils.murmurHash(builder.toString());
    }
}
