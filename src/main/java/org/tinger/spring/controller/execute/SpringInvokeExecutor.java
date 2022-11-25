package org.tinger.spring.controller.execute;

import org.tinger.common.buffer.TingerMapBuffer;
import org.tinger.core.embed.Request;
import org.tinger.core.embed.Response;
import org.tinger.spring.controller.SpringJsonInvoker;

public interface SpringInvokeExecutor {
    String type();

    Object execute(TingerMapBuffer<Integer, SpringJsonInvoker> invokerTingerMapBuffer, Request request, Response response);
}