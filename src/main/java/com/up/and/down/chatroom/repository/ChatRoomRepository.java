package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // [우리모임] memberId가 속한 채팅방 목록 조회
    @Query("""
        select
            c
        from
            ChatRoom c join c.chatRoomMembers m
        where
            m.chatRoomMemberId = :memberId
    """)
    Page<ChatRoom> findChatRoomByMemberId(@Param("memberId") User user, Pageable pageable);

    // 채팅방 참여인원수
    int countMembersByChatRoomId(Long chatRoomId);

    // [내모임] memberId가 만든 채팅방 목록 조회
    @Query("""
    select c
    from ChatRoom c
    where c.creatorId = :memberId
    """)
    Page<ChatRoom> findChatRoomCreatedByMemberId(@Param("memberId")User user, Pageable pageable);

    // 제목+내용+카테고리
    @Query("""
    select c
    from ChatRoom c join c.category cca
    where (c.name like %:keyword% or c.content like %:keyword%)
    and cca in :categories
    """)
    Page<ChatRoom> searchByNameAndContentAndCategory(@Param("keyword") String keyword, @Param("categories") Set<Category> category, Pageable pageable);
    // 제목과 내용으로 검색
    @Query("""
    select c
    from ChatRoom c
    where c.name like %:keyword% or c.content like %:keyword%
    """)
    Page<ChatRoom> searchByNameAndContent(@Param("keyword") String keyword, Pageable pageable);
    // 카테고리 검색
    @Query("""
    select c
    from ChatRoom c
    join c.category cca
    where cca in :categories
    """)
    Page<ChatRoom> searchCategory(@Param("categories") Set<Category> category, Pageable pageable);
}
