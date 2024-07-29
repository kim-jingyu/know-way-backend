package com.knowway.chat.service;

import com.knowway.chat.entity.ChatMessage;
import com.knowway.chat.repository.ChatMessageRepository;
import com.knowway.departmentstore.domain.DepartmentStore;
import com.knowway.departmentstore.repository.DepartmentStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final DepartmentStoreRepository departmentStoreRepository;

    @Override
    public ChatMessage postMessage(Long departmentStoreId, ChatMessage chatMessage) {
        DepartmentStore departmentStore = departmentStoreRepository.findById(departmentStoreId)
                .orElseThrow(() -> new RuntimeException("Department store not found"));

        chatMessage.updateDepartmentStore(departmentStore);
        chatMessage.updateCreatedAt(new Date()); // 설정된 createdAt 필드

        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findMessages(Long storeId) {
        return chatMessageRepository.findByDepartmentStoreId(storeId);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void deleteOldMessages() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        chatMessageRepository.deleteByTimestampBefore(oneDayAgo);
    }
}
