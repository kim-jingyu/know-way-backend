package com.knowway.chat.entity;

import com.knowway.common.entity.BaseEntity;
import com.knowway.departmentstore.domain.DepartmentStore;
import com.knowway.user.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "department_store_id")
    @ManyToOne
    private DepartmentStore departmentStore;

    @Column(name = "message_content", nullable = false, length = 255)
    private String messageContent;

    @Column(name = "message_nickname", nullable = false, length = 255)
    private String messageNickname;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public void updateDepartmentStore(DepartmentStore departmentStore) {
        this.departmentStore = departmentStore;
    }

    public void updateCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
  }