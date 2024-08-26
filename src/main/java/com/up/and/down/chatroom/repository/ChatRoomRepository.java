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

    /**
     * 모든 채팅방
     */
    //모든 채팅방 카테고리만 있는 경우
    Page<ChatRoom> findAllByCategoryIn(Set<Category> categories,Pageable pageable);
    //모든 채팅방 검색어만 있는 경우
    Page<ChatRoom> findAllByNameContaining(String keyword,Pageable pageable);
    //모든 채팅방에 둘다 있는 경우
    Page<ChatRoom> findAllByCategoryInAndNameContaining(Set<Category> categories, String keyword,Pageable pageable);

    /**
     * 우리 모임 - 내가 속해 있는 채팅방
     */
    // 내가 속해있는 채팅방 조회
    Page<ChatRoom> findByMemberIdListContaining(Pageable pageable,Long userId);

    //내가 속해있는 채팅방에서 카테고리만 있는 경우
    Page<ChatRoom> findByMemberIdListContainingAndCategoryIn(Pageable pageable,Long userId, Set<Category> categories);

    //내가 속해 있는 채팅방에서 카테고리 검색어 둘다 있는 경우
    Page<ChatRoom> findByMemberIdListContainingAndCategoryInAndNameContaining(Pageable pageable,Long userId, Set<Category> categories, String keyword);

    //내가 속해 있는 채팅방에서 검색어만 있는 경우
    Page<ChatRoom> findByMemberIdListContainingAndNameContaining(Pageable pageable,Long userId, String keyword);

    /**
     * 내모임 - 내가 방장인 채팅 방
     *
     */
    //내가 방장인 채팅방 조회
    Page<ChatRoom> findByCreatorId(Pageable pageable,Long userId);
    //내가 방장인 방에서 카테고리만 있는 경우
    Page<ChatRoom> findByCreatorIdAndCategoryIn(Pageable pageable,Long userId, Set<Category> categories);

    //내가 방장인 방에서 카테고리와 검색어 둘다 있는 경우
    Page<ChatRoom> findByCreatorIdAndCategoryInAndNameContaining(Pageable pageable,Long userId, Set<Category> categories, String keyword);

    //내가 방장인 방에서 검색어만 있는 경우
    Page<ChatRoom> findByCreatorIdAndNameContaining(Pageable pageable,Long userId, String keyword);

}
//
//// 채팅방 참여인원수
//@Query("""
//        select count(m)
//        from ChatRoom c join c.memberIdList m\s
//        where c.chatRoomId = :chatRoomId
//        """)
//int countMembersByChatRoomId(@Param("chatRoomId") Long chatRoomId);
//
//// member가 속한 채팅방 제목으로 검색
//@Query("""
//        select c
//        from ChatRoom c
//        where :memberId member of c.memberIdList and c.name like %:name%
//        """)
//Page<ChatRoom> findByNameContainingAndMember(@Param("name") String name, @Param("memberId") Long memberId, Pageable pageable);
//
//// member가 만든 채팅방 제목으로 검색
//@Query("""
//        select c
//        from ChatRoom c
//        where c.name like %:name% and c.creatorId = :creatorId
//        """)
//Page<ChatRoom> findByNameContainingAndCreator(@Param("name") String name, @Param("creatorId") Long creatorId, Pageable pageable);
//
//// 전체 탭에서 제목으로 검색
//@Query("""
//        select c
//        from ChatRoom c
//        where c.name like %:name%
//        """)
//Page<ChatRoom> findByNameContaining(@Param("name") String name, Pageable pageable);
//
//// member가 속한 채팅방 제목+내용으로 검색
//@Query("""
//        select c
//        from ChatRoom c
//        where :memberId member of c.memberIdList and (c.name like %:name% or c.description like %:description%)
//        """)
//Page<ChatRoom> findByNameContainingOrDescriptionContainingAndMemberId(@Param("name") String name, @Param("description") String description, @Param("memberId") Long memberId, Pageable pageable);
//
//// member가 만든 채팅방 제목+내용으로 검색
//@Query("""
//        select c
//        from ChatRoom c
//        where (c.name like %:name% or c.description like %:description%) and c.creatorId = :creatorId
//        """)
//Page<ChatRoom> findByNameContainingOrDescriptionContainingAndCreatorId(@Param("name") String name, @Param("description") String description, @Param("creatorId") Long creatorId, Pageable pageable);
//
//// 전체 탭에서 제목+내용으로 검색
//@Query("""
//        select c
//        from ChatRoom c
//        where c.name like %:name% or c.description like %:description%
//        """)
//Page<ChatRoom> findByNameContainingOrDescriptionContaining(@Param("name") String name, @Param("description") String description, Pageable pageable);
//
//// 전체 탭에서 카테고리 검색
//@Query("""
//        select c
//        from ChatRoom c join c.category cg
//        where cg in :categories
//        """)
//Page<ChatRoom> findByCategory(@Param("categories") Set<Category> categories, Pageable pageable);
//
//// member가 속한 채팅방 카테고리 검색
//@Query("""
//        select c
//        from ChatRoom c join c.category cg
//        join c.memberIdList m
//        where cg in :categories and m = :memberId
//        """)
//Page<ChatRoom> findByCategoryAndMember(@Param("categories") Set<Category> categories, @Param("memberId") Long memberId, Pageable pageable);
//
//// member가 만든 채팅방 카테고리 검색
//@Query("""
//        select c
//        from ChatRoom c join c.category cg
//        where cg in :categories and c.creatorId = :creatorId
//        """)
//Page<ChatRoom> findByCategoryCreator(@Param("categories") Set<Category> categories, @Param("creatorId") Long creatorId, Pageable pageable);
//

