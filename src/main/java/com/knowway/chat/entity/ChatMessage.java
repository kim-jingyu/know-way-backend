package com.knowway.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knowway.common.entity.BaseEntity;
import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.user.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    @Column(name = "member_chat_id",updatable = false)
    private Long memberChatId;

    @JoinColumn(name = "department_store_id")
    @ManyToOne
    @JsonIgnore
    private DepartmentStore departmentStore;

    @Column(name = "message_content", nullable = false, length = 255)
    private String messageContent;

    @Column(name = "message_nickname", nullable = false, length = 255)
    private String messageNickname;

}
