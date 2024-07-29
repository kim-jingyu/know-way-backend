package com.knowway.chat.controller;

import com.knowway.chat.entity.ChatMessage;
import com.knowway.chat.service.ChatMessageService;
import com.knowway.chat.service.ChatMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/chats")
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/{departmentStoreId}/messages")
    public List<ChatMessage> messagesList(@PathVariable Long departmentStoreId) {
        return chatMessageService.findMessages(departmentStoreId);
    }

    @PostMapping("/{departmentStoreId}/messages")
    public ChatMessage postMessage(@PathVariable Long departmentStoreId, @RequestBody ChatMessage message) {
        return chatMessageService.postMessage(departmentStoreId, message);
    }

}
