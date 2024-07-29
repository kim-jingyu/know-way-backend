package com.knowway.chat.service;

import com.knowway.chat.entity.ChatMessage;
import com.knowway.departmentstore.domain.DepartmentStore;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessage> findMessages(Long storeId);
    @Scheduled void deleteOldMessages();
    ChatMessage postMessage(Long departmentStoreId, ChatMessage chatMessage);
}
