package com.example.game.config;

import com.example.game.controller.MyWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/connect")
//                .setAllowedOrigins("*")
////                .setHandshakeHandler(new MyHandshakeHandler())
//                .withSockJS();
//    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new MyChannelInterceptor());
//    }
//}

//@Configuration
//public class WebSocketConfig implements WebSocketConfigurer {
////    @Bean
////    public ServerEndpointExporter serverEndpointExporter() {
////        return new ServerEndpointExporter();
////    }
//    @Autowired
//    private MyWebSocketServer server;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(server, "/connect")
//                .setAllowedOrigins("*");
//    }
//}

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public CustomSpringConfigurator customSpringConfigurator() {
        return new CustomSpringConfigurator();
    }
}