package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    // 메시지 내역 불러오기
    @Query("""
    select c
    from Chat c
    where c.chatRoomId = :chatRoomId
    order by c.createdAt asc
    """)
    List<Chat> findChatMessageByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

    // 최근 작성된 채팅 메시지 가져오기
    @Query("""
    select c
    from Chat c
    where c.chatRoomId = :chatRoomId
    order by c.createdAt asc 
    """)
    List<Chat> findLastChatByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);
}
