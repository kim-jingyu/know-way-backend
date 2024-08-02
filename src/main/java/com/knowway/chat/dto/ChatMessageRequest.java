package com.knowway.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatMessageRequest {
    private Long memberChatId;
    private Long departmentStoreId;
    private String messageNickname;
    private String messageContent;
}
