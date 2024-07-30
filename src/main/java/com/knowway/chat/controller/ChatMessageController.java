package com.knowway.chat.controller;

import com.knowway.chat.entity.ChatMessage;
import com.knowway.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping(value = "/chats")
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/{departmentStoreId}")
    public List<ChatMessage> messagesList(@PathVariable("departmentStoreId") Long departmentStoreId) {
        return chatMessageService.findMessages(departmentStoreId);
    }

    @PostMapping("/{departmentStoreId}/messages")
    public ResponseEntity<String> postMessage(@PathVariable("departmentStoreId") Long departmentStoreId, @RequestBody ChatMessage message) {
        chatMessageService.postMessage(departmentStoreId, message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("채팅 메시지가 정상적으로 전송되었습니다.");
    }

}
