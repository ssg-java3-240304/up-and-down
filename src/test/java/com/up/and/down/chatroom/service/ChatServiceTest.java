package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.repository.ChatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class ChatServiceTest {
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @DisplayName("채팅 메시지 불러오기")
    void findChatMessageByChatRoomId() {
        // given
        List<Chat> chatList = chatRepository.findChatMessageByChatRoomId(1L);
        // when
        List<ChatDto> chatDtos = chatList.stream().map(ChatDto::toChatDto).toList();
        // then
        System.out.println(chatDtos);
    }
}