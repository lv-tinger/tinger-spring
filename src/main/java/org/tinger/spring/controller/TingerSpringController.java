package org.tinger.spring.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.core.apps.Application;
import org.tinger.core.embed.Controller;
import org.tinger.core.embed.EmbedModule;
import org.tinger.core.embed.Request;
import org.tinger.core.embed.Response;
import org.tinger.spring.controller.execute.SpringInvokeExecutor;

import java.util.ArrayList;
import java.util.List;

@Component
public class TingerSpringController implements Controller, InitializingBean, ApplicationContextAware {

    private final TingerMapBuffer<Integer, SpringJsonInvoker> invokerTingerMapBuffer = new TingerMapBuffer<>();

    private final TingerMapBuffer<String, SpringInvokeExecutor> executor = new TingerMapBuffer<>();

    private ApplicationContext applicationContext;

    void register(SpringJsonInvoker springJsonInvoker) {
        invokerTingerMapBuffer.putIfAbsent(springJsonInvoker.getId(), springJsonInvoker);
    }

    @Override
    public String path() {
        return "/spring.api";
    }

    @Override
    public Object execute(Request request, Response response) {
        String type = request.string("type");
        SpringInvokeExecutor springInvokeExecutor = this.executor.get(type);
        if(springInvokeExecutor == null){
            return "失败";
        }
        return springInvokeExecutor.execute(invokerTingerMapBuffer, request, response);
    }

    @Override
    public void afterPropertiesSet() {
        Application.getInstance().module(EmbedModule.class).provide().provide().controller(this);
        List<SpringInvokeExecutor> executors = new ArrayList<>(this.applicationContext.getBeansOfType(SpringInvokeExecutor.class).values());
        for (SpringInvokeExecutor executor : executors) {
            this.executor.putIfAbsent(executor.type(), executor);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Getter
    @Setter
    @Builder
    public static class JsonInvokerView {
        private Integer invokerId;
        private String className;
        private String methodName;
        private List<String> parameterTypes;
    }
}