package com.knowway.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatMessageResponse {
    private Long memberId;
    private LocalDateTime createdAt;
    private String messageNickname;
    private String messageContent;
}
