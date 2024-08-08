package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
        select 
            c
        from
            ChatRoom c join c.chatRoomMembers m 
        where
            m.chatRoomMemberId = :memberId
    """)
    List<ChatRoom> findChatRoomByMemberId(@Param("memberId") Long memberId);

    // 채팅방 참여인원수
    int countMembersByChatRoomId(Long chatRoomId);
}
