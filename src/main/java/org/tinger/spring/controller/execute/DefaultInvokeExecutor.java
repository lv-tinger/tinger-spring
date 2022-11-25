package org.tinger.spring.controller.execute;

import org.springframework.stereotype.Component;
import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.common.serialize.JsonSerializer;
import org.tinger.common.utils.ConverterUtil;
import org.tinger.common.utils.StringUtils;
import org.tinger.core.embed.Request;
import org.tinger.core.embed.Response;
import org.tinger.spring.controller.SpringJsonInvoker;

@Component
public class DefaultInvokeExecutor implements SpringInvokeExecutor {
    @Override
    public String type() {
        return "default";
    }

    @Override
    public Object execute(TingerMapBuffer<Integer, SpringJsonInvoker> invokerTingerMapBuffer, Request request, Response response) {
        int id = request.integer("id");
        SpringJsonInvoker invoker = invokerTingerMapBuffer.get(id);
        if (invoker == null) {
            throw new RuntimeException("请求不存在");
        }
        Class<?>[] parameterTypes = invoker.getParameters();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return invoker.invoke();
        }

        int length = parameterTypes.length;

        Object[] parameterValues = new Object[parameterTypes.length];
        for (int i = 0; i < length; i++) {
            Object parameterValue = null;
            String stringValue = request.string("param_" + i);
            if (StringUtils.isNotEmpty(stringValue)) {
                if (String.class.equals(parameterTypes[i])) {
                    parameterValue = stringValue;
                } else if (Integer.class.equals(parameterTypes[i])) {
                    parameterValue = ConverterUtil.toInteger(stringValue);
                } else if (Boolean.class.equals(parameterTypes[i])) {
                    parameterValue = ConverterUtil.toBoolean(stringValue);
                } else if (Long.class.equals(parameterTypes[i])) {
                    parameterValue = ConverterUtil.toLong(stringValue);
                } else {
                    parameterValue = JsonSerializer.getInstance().fromJson(stringValue, parameterTypes[i]);
                }
            }
            parameterValues[i] = parameterValue;
        }

        return invoker.invoke(parameterValues);
    }
}