package com.knowway.chat.repository;

import com.knowway.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    void deleteByCreatedAtBefore(LocalDateTime created_at);
    Page<ChatMessage> findByDepartmentStore_DepartmentStoreIdOrderByCreatedAtDesc(Long departmentStoreId, Pageable pageable);
}
