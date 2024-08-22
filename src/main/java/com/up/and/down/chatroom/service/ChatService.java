package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.repository.ChatRepository;
import com.up.and.down.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRepository chatRepository;

    // 메시지 db에 저장
    public void saveMessage(ChatDto chatDto) {
        Chat chat = chatDto.toChatEntity();
        Chat savedChat = chatRepository.save(chat); // 저장후 반환된 엔티티 사용하기
        log.debug("Saved chatId: {}", savedChat.getId());

        // 저장된 메시지 정보 사용해서 chatDto 갱신하기
        ChatDto savedChatDto = ChatDto.toChatDto(savedChat);

        // ChatDto의 id를 로그로 출력하여 확인
        log.debug("ChatDto ID after saving: {}", savedChatDto.getId());

        messagingTemplate.convertAndSend("/sub/chat-rooms/" + chatDto.getChatRoomId(), savedChatDto);
    }

    // chatRoomId로 모든 채팅 메시지 가져오기
    public List<ChatDto> findChatMessageByChatRoomId(Long chatRoomId, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        List<Chat> chatPage = chatRepository.findChatMessageByChatRoomId(chatRoomId, pageable);
        // 로그 추가
        List<ChatDto> chatDtos = chatPage.stream().map(ChatDto::toChatDto).collect(Collectors.toList());
        chatDtos.forEach(chatDto -> log.debug("ChatDto ID: {}", chatDto.getId()));
        return chatPage.stream().map(ChatDto::toChatDto).collect(Collectors.toList());
    }

    // 최근 메시지 가져오기
    public List<ChatDto> findLastChatByChatRoomId(Long chatRoomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Chat> chatList = chatRepository.findLastChatByChatRoomId(chatRoomId, pageable);
        // 로그 추가
        List<ChatDto> chatDtos = chatList.stream().map(ChatDto::toChatDto).collect(Collectors.toList());
        chatDtos.forEach(chatDto -> log.debug("ChatDto ID: {}", chatDto.getId()));
        return chatList.stream().map(ChatDto::toChatDto).collect(Collectors.toList());
    }
}
