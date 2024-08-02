package com.knowway.chat.repository;

import com.knowway.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByDepartmentStore_DepartmentStoreIdOrderByCreatedAt(Long departmentStoreId);
    void deleteByCreatedAtBefore(LocalDateTime created_at);
}
