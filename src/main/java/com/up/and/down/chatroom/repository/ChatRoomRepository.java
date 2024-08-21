package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.dto.ShowChatRoomDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

//     채팅방 조회

    // 내가 속해있는 채팅방 조회
    List<ChatRoom> findByMemberIdListContaining(Long userId);
    // 채팅방 참여인원수
    @Query("""
        select count(m)
        from ChatRoom c join c.memberIdList m\s
        where c.chatRoomId = :chatRoomId
        """)
    int countMembersByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    // member가 속한 채팅방 제목으로 검색
    @Query("""
        select c
        from ChatRoom c
        where :memberId member of c.memberIdList and c.name like %:name%
        """)
    Page<ChatRoom> findByNameContainingAndMember(@Param("name") String name, @Param("memberId") Long memberId, Pageable pageable);

    // member가 만든 채팅방 제목으로 검색
    @Query("""
        select c
        from ChatRoom c
        where c.name like %:name% and c.creatorId = :creatorId
        """)
    Page<ChatRoom> findByNameContainingAndCreator(@Param("name") String name, @Param("creatorId") Long creatorId, Pageable pageable);

    // 전체 탭에서 제목으로 검색
    @Query("""
        select c
        from ChatRoom c
        where c.name like %:name%
        """)
    Page<ChatRoom> findByNameContaining(@Param("name") String name, Pageable pageable);

    // member가 속한 채팅방 제목+내용으로 검색
    @Query("""
        select c
        from ChatRoom c
        where :memberId member of c.memberIdList and (c.name like %:name% or c.description like %:description%)
        """)
    Page<ChatRoom> findByNameContainingOrDescriptionContainingAndMemberId(@Param("name") String name, @Param("description") String description, @Param("memberId") Long memberId, Pageable pageable);

    // member가 만든 채팅방 제목+내용으로 검색
    @Query("""
        select c
        from ChatRoom c
        where (c.name like %:name% or c.description like %:description%) and c.creatorId = :creatorId
        """)
    Page<ChatRoom> findByNameContainingOrDescriptionContainingAndCreatorId(@Param("name") String name, @Param("description") String description, @Param("creatorId") Long creatorId, Pageable pageable);

    // 전체 탭에서 제목+내용으로 검색
    @Query("""
        select c
        from ChatRoom c
        where c.name like %:name% or c.description like %:description%
        """)
    Page<ChatRoom> findByNameContainingOrDescriptionContaining(@Param("name") String name, @Param("description") String description, Pageable pageable);

    // 전체 탭에서 카테고리 검색
    @Query("""
        select c
        from ChatRoom c join c.category cg
        where cg in :categories
        """)
    Page<ChatRoom> findByCategory(@Param("categories") Set<Category> categories, Pageable pageable);

    // member가 속한 채팅방 카테고리 검색
    @Query("""
        select c
        from ChatRoom c join c.category cg
        join c.memberIdList m
        where cg in :categories and m = :memberId
        """)
    Page<ChatRoom> findByCategoryAndMember(@Param("categories") Set<Category> categories, @Param("memberId") Long memberId, Pageable pageable);

    // member가 만든 채팅방 카테고리 검색
    @Query("""
        select c
        from ChatRoom c join c.category cg
        where cg in :categories and c.creatorId = :creatorId
        """)
    Page<ChatRoom> findByCategoryCreator(@Param("categories") Set<Category> categories, @Param("creatorId") Long creatorId, Pageable pageable);


}
