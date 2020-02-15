package com.example.game.config;

import org.apache.naming.factory.BeanFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

public class CustomSpringConfigurator extends ServerEndpointRegistration.Configurator implements ApplicationContextAware {
    private static volatile ApplicationContext context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationError {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        context = context;
        CustomSpringConfigurator.context = applicationContext;
//        this.context = applicationContext;
    }
}
