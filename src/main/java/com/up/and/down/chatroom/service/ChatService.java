package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.repository.ChatRepository;
import com.up.and.down.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 메시지 db에 저장
    public void saveMessage(ChatDto chatDto) {
        Chat chat = chatDto.toChatEntity();
        chatRepository.save(chat);
    }

    // 메시지 내역 불러오기
    public List<ChatDto> findChatMessageByChatRoomId(Long chatRoomId){
        List<Chat> chatList = chatRepository.findChatMessageByChatRoomId(chatRoomId);
        return chatList.stream().map(ChatDto::toChatDto).toList();// List<ChatDto>로 수집
    }
}
