package com.knowway.chat.service;

import com.knowway.chat.dto.ChatMessageRequest;
import com.knowway.chat.dto.ChatMessageResponse;
import com.knowway.chat.entity.ChatMessage;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

public interface ChatMessageService {
    List<ChatMessageResponse> findMessages(Long storeId);
    @Scheduled void deleteOldMessages();
    ChatMessage postMessage(ChatMessageRequest chatMessageRequest);
}
