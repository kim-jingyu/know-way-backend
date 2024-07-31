package com.knowway.chat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String requestURI = request.getURI().toString();
        String[] uriParts = requestURI.split("/");
        System.out.println("Request URI: " + requestURI);
        if (uriParts.length > 3) {
            int i = 0;
            for (String part : uriParts) {
                System.out.println("i = " + i + " part = " + part);
                i++;
            }
            String departmentStoreId = uriParts[5];
            attributes.put("departmentStoreId", departmentStoreId);
        } else {
            return false; // Invalid URI
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}