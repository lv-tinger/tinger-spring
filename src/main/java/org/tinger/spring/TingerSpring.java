package org.tinger.spring;

import org.springframework.context.ApplicationContext;
import org.tinger.core.spring.Spring;

import java.util.Map;

public class TingerSpring implements Spring {
    private final ApplicationContext context;

    public TingerSpring(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    @Override
    public <T> Map<String, T> getBeans(Class<T> type) {
        return context.getBeansOfType(type);
    }
}
