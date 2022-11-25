package org.tinger.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.tinger.core.apps.Application;

@Component
public class SpringApplicationBeanFactory implements FactoryBean<Application> {
    @Override
    public Application getObject() {
        return Application.getInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return Application.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
