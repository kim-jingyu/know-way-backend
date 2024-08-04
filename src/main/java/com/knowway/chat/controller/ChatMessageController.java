package com.knowway.chat.controller;

import com.knowway.chat.dto.ChatMessageRequest;
import com.knowway.chat.dto.ChatMessageResponse;
import org.springframework.data.domain.Page;
import com.knowway.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(value = "/chats")
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/{departmentStoreId}")
    public ResponseEntity<Page<ChatMessageResponse>> messagesList(
            @PathVariable("departmentStoreId") Long departmentStoreId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok(chatMessageService.findMessages(departmentStoreId, page, size));
    }

    @PostMapping
    public ResponseEntity<String> postMessage(@RequestBody ChatMessageRequest message) {
        chatMessageService.postMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("채팅 메시지가 정상적으로 전송되었습니다.");
    }

}
