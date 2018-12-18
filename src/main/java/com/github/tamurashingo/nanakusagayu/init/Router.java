package com.github.tamurashingo.nanakusagayu.init;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class Router extends AbstractHandler {

    /**
     * ルーティング情報
     * String: /path
     * Object[0]: Controller instance
     * Object[1]: method instance
     */
    private Map<String, Object[]> routing;

    public Router(Map<String, Object[]> routing) {
        this.routing = routing;
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        if (!routing.containsKey(s)) {
            throw new ServletException("page not found");
        }
        Object[] inst = routing.get(s);

        try {
            Method method = (Method) inst[1];
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            method.invoke(inst[0], httpServletRequest, httpServletResponse);
            request.setHandled(true);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }
}
