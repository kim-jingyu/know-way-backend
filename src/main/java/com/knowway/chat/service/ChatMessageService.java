package com.knowway.chat.service;

import com.knowway.chat.entity.ChatMessage;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;

public interface ChatMessageService {
    List<ChatMessage> findMessages(Long storeId);
    @Scheduled void deleteOldMessages();
    ChatMessage postMessage(Long departmentStoreId, ChatMessage chatMessage);
}
