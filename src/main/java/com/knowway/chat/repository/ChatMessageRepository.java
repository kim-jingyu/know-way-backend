package com.knowway.chat.repository;

import com.knowway.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByDepartmentStoreId(Long departmentStoreId);
    void deleteByTimestampBefore(LocalDateTime timestamp);
}
