package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatroomId(Long chatroomId, Pageable pageable);
}
