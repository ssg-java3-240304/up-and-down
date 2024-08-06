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
        assertThat(savedChat.getMessage()).isEqualTo("안녕하세여. 야호입니다");
        // 실제로 db에 저장되었는 지 확인해보기
        Chat foundChat = chatRepository.findById(savedChat.getId()).orElse(null);
        assertThat(foundChat).isNotNull();
        assertThat(foundChat.getChatRoomId()).isEqualTo(1L);
        assertThat(foundChat.getNickname()).isEqualTo("야호");
        assertThat(foundChat.getMessage()).isEqualTo("안녕하세여. 야호입니다");
    }

    @Test
    @DisplayName("메시지 내역 불러오기")
    void test2() {
        // given
        ChatDto chatDto1 = new ChatDto();
        chatDto1.setChatRoomId(1L);
        chatDto1.setNickname("전");
        chatDto1.setMessage("현선입니다");

        ChatDto chatDto2 = new ChatDto();
        chatDto2.setChatRoomId(1L);
        chatDto2.setNickname("야");
        chatDto2.setMessage("호입니다");

        chatService.saveMessage(chatDto1);
        chatService.saveMessage(chatDto2);
        // when
        List<ChatDto> chatList = chatService.findChatMessageByChatRoomId(1L);
        // then
        assertThat(chatList).hasSize(2);
        assertThat(chatList).extracting(ChatDto::getMessage).containsExactly("현선입니다", "호입니다");
    }
}