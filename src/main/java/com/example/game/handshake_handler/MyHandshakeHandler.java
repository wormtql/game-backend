package com.example.game.handshake_handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

public class MyHandshakeHandler extends DefaultHandshakeHandler {
    private HttpServletRequest getServletRequest(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            return serverHttpRequest.getServletRequest();
        }

        return null;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest serverRequest, WebSocketHandler handler,
                                      Map<String, Object> attributes) {
        HttpServletRequest request = getServletRequest(serverRequest);
        final int userId = (Integer) request.getAttribute("identity");
        return new Principal() {
            @Override
            public String getName() {
                return Integer.toString(userId);
            }
        };
    }
}
