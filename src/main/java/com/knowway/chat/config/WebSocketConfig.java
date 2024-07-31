package com.knowway.chat.config;

import com.knowway.chat.handler.ChatWebSocketHandler;
import com.knowway.chat.interceptor.CustomHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(), "/ws/chat/{departmentStoreId}")
                .setAllowedOrigins("*")
                .addInterceptors(new CustomHandshakeInterceptor());
    }
}