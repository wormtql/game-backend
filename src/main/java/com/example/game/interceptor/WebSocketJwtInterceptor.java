package com.example.game.interceptor;

import com.example.game.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebSocketJwtInterceptor implements HandshakeInterceptor {
    private HttpServletRequest getServletRequest(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            return serverHttpRequest.getServletRequest();
        }

        return null;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverRequest, ServerHttpResponse response, WebSocketHandler handler,
                                   Map<String, Object> attributes) throws Exception {
        HttpServletRequest request = getServletRequest(serverRequest);
        String jwt = request.getHeader("Authorization");
        Claims claims = JwtUtil.verifyJwt(jwt);
        if (claims == null) {
            return false;
        }

        request.setAttribute("identity", claims.get("userId"));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
                               Exception e) {

    }
}
