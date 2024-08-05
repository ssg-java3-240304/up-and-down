package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.repository.ChatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChatServiceTest {
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @DisplayName("메시지 등록")
    void test1(){
        // given
        ChatDto chatDto = new ChatDto();
        chatDto.setChatRoomId(1L);
        chatDto.setNickname("야호");
        chatDto.setMessage("안녕하세여. 야호입니다");
        // when
        Chat savedChat = chatService.saveMessage(chatDto);
        // then
        assertThat(savedChat.getId()).isNotNull();
        assertThat(savedChat.getChatRoomId()).isEqualTo(1L);
        assertThat(savedChat.getNickname()).isEqualTo("야호");
        assertThat(savedChat.getMessage()).isEqualTo("안녕하세요. 야호입니다.");
    }
}