package com.knowway.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowway.chat.dto.ChatMessageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, CopyOnWriteArrayList<WebSocketSession>> sessionsMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String departmentStoreId = getDepartmentStoreId(session);
        try {
            sessionsMap.computeIfAbsent(departmentStoreId, k -> new CopyOnWriteArrayList<>()).add(session);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지를 JSON으로 변환하여 departmentStoreId를 추출
        ChatMessageRequest chatMessageRequest = objectMapper.readValue(message.getPayload(), ChatMessageRequest.class);
        String departmentStoreId = chatMessageRequest.getDepartmentStoreId().toString();

        // 세션 속성에 departmentStoreId를 설정
        session.getAttributes().put("departmentStoreId", departmentStoreId);

        // 해당 지점의 모든 세션에 메시지 브로드캐스트
        for (WebSocketSession webSocketSession : sessionsMap.computeIfAbsent(departmentStoreId, k -> new CopyOnWriteArrayList<>())) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String departmentStoreId = getDepartmentStoreId(session);
        sessionsMap.getOrDefault(departmentStoreId, new CopyOnWriteArrayList<>()).remove(session);
    }

    private String getDepartmentStoreId(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        return (String) attributes.get("departmentStoreId");
    }
}