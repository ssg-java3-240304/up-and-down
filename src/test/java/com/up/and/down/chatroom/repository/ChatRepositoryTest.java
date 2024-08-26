package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.Chat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChatRepositoryTest {
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @DisplayName("전체 조회")
    void testFindAll() {
        // when
        List<Chat> chatList = chatRepository.findAll();

        // then
        assertThat(chatList).isNotNull();
        assertThat(chatList).isNotEmpty();

        chatPrint(chatList);
    }

    private void chatPrint(List<Chat> chatList) {
        System.out.println("=== 조회된 채팅 기록 ===");
        for (Chat chat : chatList) {
            System.out.println("ID: " + chat.getId());
            System.out.println("채팅방 ID: " + chat.getChatroomId());
            System.out.println("멤버 ID: " + chat.getMemberId());
            System.out.println("닉네임: " + chat.getNickname());
            System.out.println("메시지: " + chat.getMessage());
            System.out.println("생성 시간: " + chat.getCreatedAt());
            System.out.println("----------------------------");
        }
    }
}