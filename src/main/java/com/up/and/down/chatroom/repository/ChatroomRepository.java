package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    /**
     * 모든 채팅방
     */
    //모든 채팅방 카테고리만 있는 경우
    List<Chatroom> findAllByCategoryIn(Set<Category> categories);
    //모든 채팅방 검색어만 있는 경우
    List<Chatroom> findAllByNameContaining(String keyword);
    //모든 채팅방에 둘다 있는 경우
    List<Chatroom> findAllByCategoryInAndNameContaining(Set<Category> categories, String keyword);
    // 채팅방 참여인원수
    @Query("""
    select size(c.memberIdList)
    from ChatRoom c
    where c.chatroomId = :chatRoomId
    """)
    int countMembersByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    /**
     * 우리 모임 - 내가 속해 있는 채팅방
     */
    // 내가 속해있는 채팅방 조회
    List<Chatroom> findByMemberIdListContaining(Long userId);
    // member가 속한 채팅방 제목으로 검색
    @Query("""
    select c
    from ChatRoom c
    where :memberId member of c.memberIdList and c.name like %:name%
    """)
    Page<Chatroom> findByNameContainingAndMember(@Param("name") String name, @Param("memberId") Long memberId, Pageable pageable);

    //내가 속해있는 채팅방에서 카테고리만 있는 경우
    List<Chatroom> findByMemberIdListContainingAndCategoryIn(Long userId, Set<Category> categories);
    // member가 만든 채팅방 제목으로 검색
    @Query("""
    select c
    from ChatRoom c
    where c.name like %:name% and c.creatorId = :creatorId
    """)
    Page<Chatroom> findByNameContainingAndCreator(@Param("name") String name, @Param("creatorId") Long creatorId, Pageable pageable);

    //내가 속해 있는 채팅방에서 카테고리 검색어 둘다 있는 경우
    List<Chatroom> findByMemberIdListContainingAndCategoryInAndNameContaining(Long userId, Set<Category> categories, String keyword);
    // 전체 탭에서 제목으로 검색
    @Query("""
    select c
    from ChatRoom c
    where c.name like %:name%
    """)
    Page<Chatroom> findByNameContaining(@Param("name") String name, Pageable pageable);

    //내가 속해 있는 채팅방에서 검색어만 있는 경우
    List<Chatroom> findByMemberIdListContainingAndNameContaining(Long userId, String keyword);
    // member가 속한 채팅방 제목+내용으로 검색
    @Query("""
    select c
    from ChatRoom c
    where :memberId member of c.memberIdList and (c.name like %:name% or c.description like %:description%)
    """)
    Page<Chatroom> findByNameContainingOrDescriptionContainingAndMemberId(@Param("name") String name, @Param("description") String description, @Param("memberId") Long memberId, Pageable pageable);

    /**
     * 내모임 - 내가 방장인 채팅 방
     *
     */
    //내가 방장인 채팅방 조회
    List<Chatroom> findByCreatorId(Long userId);
    //내가 방장인 방에서 카테고리만 있는 경우
    List<Chatroom> findByCreatorIdAndCategoryIn(Long userId, Set<Category> categories);
    // member가 만든 채팅방 제목+내용으로 검색
    @Query("""
    select c
    from ChatRoom c
    where (c.name like %:name% or c.description like %:description%) and c.creatorId = :creatorId
    """)
    Page<Chatroom> findByNameContainingOrDescriptionContainingAndCreatorId(@Param("name") String name, @Param("description") String description, @Param("creatorId") Long creatorId, Pageable pageable);

    //내가 방장인 방에서 카테고리와 검색어 둘다 있는 경우
    List<Chatroom> findByCreatorIdAndCategoryInAndNameContaining(Long userId, Set<Category> categories, String keyword);
    // 전체 탭에서 제목+내용으로 검색
    @Query("""
    select c
    from ChatRoom c
    where c.name like %:name% or c.description like %:description%
    """)
    Page<Chatroom> findByNameContainingOrDescriptionContaining(@Param("name") String name, @Param("description") String description, Pageable pageable);

    //내가 방장인 방에서 검색어만 있는 경우
    List<Chatroom> findByCreatorIdAndNameContaining(Long userId, String keyword);
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
