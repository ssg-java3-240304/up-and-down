package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    // 메시지 내역 불러오기
    List<Chat> findByChatRoomId(Long chatRoomId);
}
