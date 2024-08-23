package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    // 채팅방 참여인원수
    @Query("""
    select size(c.memberIdList)
    from ChatRoom c
    where c.chatroomId = :chatRoomId
    """)
    int countMembersByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    // member가 속한 채팅방 제목으로 검색
    @Query("""
    select c
    from ChatRoom c
    where :memberId member of c.memberIdList and c.name like %:name%
    """)
    Page<Chatroom> findByNameContainingAndMember(@Param("name") String name, @Param("memberId") Long memberId, Pageable pageable);

    // member가 만든 채팅방 제목으로 검색
    @Query("""
    select c
    from ChatRoom c
    where c.name like %:name% and c.creatorId = :creatorId
    """)
    Page<Chatroom> findByNameContainingAndCreator(@Param("name") String name, @Param("creatorId") Long creatorId, Pageable pageable);

    // 전체 탭에서 제목으로 검색
    @Query("""
    select c
    from ChatRoom c
    where c.name like %:name%
    """)
    Page<Chatroom> findByNameContaining(@Param("name") String name, Pageable pageable);

    // member가 속한 채팅방 제목+내용으로 검색
    @Query("""
    select c
    from ChatRoom c
    where :memberId member of c.memberIdList and (c.name like %:name% or c.description like %:description%)
    """)
    Page<Chatroom> findByNameContainingOrDescriptionContainingAndMemberId(@Param("name") String name, @Param("description") String description, @Param("memberId") Long memberId, Pageable pageable);

    // member가 만든 채팅방 제목+내용으로 검색
    @Query("""
    select c
    from ChatRoom c
    where (c.name like %:name% or c.description like %:description%) and c.creatorId = :creatorId
    """)
    Page<Chatroom> findByNameContainingOrDescriptionContainingAndCreatorId(@Param("name") String name, @Param("description") String description, @Param("creatorId") Long creatorId, Pageable pageable);

    // 전체 탭에서 제목+내용으로 검색
    @Query("""
    select c
    from ChatRoom c
    where c.name like %:name% or c.description like %:description%
    """)
    Page<Chatroom> findByNameContainingOrDescriptionContaining(@Param("name") String name, @Param("description") String description, Pageable pageable);

    // 전체 탭에서 카테고리 검색
    @Query("""
    select c
    from ChatRoom c join c.category cg
    where cg in :categories
    """)
    Page<Chatroom> findByCategory(@Param("categories") Set<Category> categories, Pageable pageable);

    // member가 속한 채팅방 카테고리 검색
    @Query("""
    select c
    from ChatRoom c join c.category cg
    join c.memberIdList m
    where cg in :categories and m = :memberId
    """)
    Page<Chatroom> findByCategoryAndMember(@Param("categories") Set<Category> categories, @Param("memberId") Long memberId, Pageable pageable);

    // member가 만든 채팅방 카테고리 검색
    @Query("""
    select c
    from ChatRoom c join c.category cg
    where cg in :categories and c.creatorId = :creatorId
    """)
    Page<Chatroom> findByCategoryCreator(@Param("categories") Set<Category> categories, @Param("creatorId") Long creatorId, Pageable pageable);

}