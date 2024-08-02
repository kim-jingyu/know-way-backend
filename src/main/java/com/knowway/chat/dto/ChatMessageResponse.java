package com.knowway.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatMessageResponse {
    private Long memberChatId;
    private Long messageId;
    private LocalDateTime createdAt;
    private String messageNickname;
    private String messageContent;
}
