package org.tinger.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tinger.core.apps.ApplicationWeaverModule;
import org.tinger.core.apps.Provider;
import org.tinger.core.apps.provider.SimpleProvider;
import org.tinger.core.conf.ConfigModule;
import org.tinger.core.spring.Spring;
import org.tinger.core.spring.SpringModule;

import java.util.List;

public class TingerSpringModule extends ApplicationWeaverModule implements SpringModule {
    private Spring spring;

    private ClassPathXmlApplicationContext context;

    @Override
    public void install() {
        List<String> configs = application.module(ConfigModule.class).provide().provide().list("spring_configs", String.class);
        context = new ClassPathXmlApplicationContext(configs.toArray(new String[0]));
        context.start();
        this.spring = new TingerSpring(context);
        this.application.produce("SPRING-STARTED", this.spring);
    }

    @Override
    public void destroy() {
        context.close();
    }

    @Override
    public Provider<Spring> provide() {
        return new SimpleProvider<>(spring);
    }
}
