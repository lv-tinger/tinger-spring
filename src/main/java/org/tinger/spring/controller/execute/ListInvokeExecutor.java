package org.tinger.spring.controller.execute;

import org.springframework.stereotype.Component;
import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.utils.CollectionUtils;
import org.tinger.core.embed.Request;
import org.tinger.core.embed.Response;
import org.tinger.spring.controller.SpringJsonInvoker;
import org.tinger.spring.controller.TingerSpringController;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ListInvokeExecutor implements SpringInvokeExecutor {
    @Override
    public String type() {
        return "list";
    }

    @Override
    public Object execute(TingerMapBuffer<Integer, SpringJsonInvoker> invokerTingerMapBuffer, Request request, Response response) {
        List<SpringJsonInvoker> invokers = new ArrayList<>(invokerTingerMapBuffer.values());
        if (CollectionUtils.isEmpty(invokers)) {
            return Collections.emptyList();
        }
        LinkedList<TingerSpringController.JsonInvokerView> views = new LinkedList<>();
        for (SpringJsonInvoker invoker : invokers) {
            TingerSpringController.JsonInvokerView view = TingerSpringController.JsonInvokerView.builder().invokerId(invoker.getId()).className(invoker.getBeanType().getName()).methodName(invoker.getMethod().getName()).parameterTypes(Arrays.stream(invoker.getParameters()).map(Class::getName).collect(Collectors.toList())).build();
            views.add(view);
        }
        return views;
    }
}
