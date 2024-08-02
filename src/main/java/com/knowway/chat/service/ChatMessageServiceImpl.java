package com.knowway.chat.service;

import com.knowway.chat.dto.ChatMessageRequest;
import com.knowway.chat.dto.ChatMessageResponse;
import com.knowway.chat.entity.ChatMessage;
import com.knowway.chat.repository.ChatMessageRepository;
import com.knowway.departmentstore.entity.DepartmentStore;
import com.knowway.departmentstore.repository.DepartmentStoreRepository;
import com.knowway.user.entity.Member;
import com.knowway.user.exception.UserException;
import com.knowway.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final DepartmentStoreRepository departmentStoreRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public ChatMessage postMessage(ChatMessageRequest chatMessageRequest) {
        DepartmentStore departmentStore = departmentStoreRepository.getById(chatMessageRequest.getDepartmentStoreId());
        Member member = memberRepository.findByChatMessageId(chatMessageRequest.getMemberChatId())
                .orElseThrow(() -> new UserException("유효하지 않은 아이디입니다."));
        ChatMessage chatMessage = ChatMessage.builder()
                .member(member)
                .departmentStore(departmentStore)
                .messageContent(chatMessageRequest.getMessageContent())
                .messageNickname(chatMessageRequest.getMessageNickname())
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessageResponse> findMessages(Long departmentStoreId) {
        List<ChatMessage> messages = chatMessageRepository.findByDepartmentStore_DepartmentStoreIdOrderByCreatedAt(departmentStoreId);
        return messages.stream()
                .map(message -> new ChatMessageResponse(
                        message.getMember().getChatMessageId(),
                        message.getMessageId(),
                        message.getCreatedAt(),
                        message.getMessageNickname(),
                        message.getMessageContent()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void deleteOldMessages() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        chatMessageRepository.deleteByCreatedAtBefore(oneDayAgo);
    }
}
