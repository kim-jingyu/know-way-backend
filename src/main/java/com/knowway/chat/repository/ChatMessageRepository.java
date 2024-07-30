package com.knowway.chat.repository;

import com.knowway.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findById(Long departmentStoreId);
    void deleteByCreatedAtBefore(LocalDateTime created_at);
}
