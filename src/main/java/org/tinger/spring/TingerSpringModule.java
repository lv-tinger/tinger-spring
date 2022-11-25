package org.tinger.spring;

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

    @Override
    public void install() {
        List<String> configs = application.module(ConfigModule.class).provide().provide().list("spring_configs", String.class);
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(configs.toArray(new String[0]));
        applicationContext.start();
        this.spring = new TingerSpring(applicationContext);
        this.application.produce("SPRING-STARTED", this.spring);
    }

    @Override
    public void destroy() {

    }

    @Override
    public Provider<Spring> provide() {
        return new SimpleProvider<>(spring);
    }
}
