package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.dto.ChatroomDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    /**
     * 모든 채팅방
     */
    //모든 채팅방 카테고리만 있는 경우
    Page<Chatroom> findAllByCategoryIn(Set<Category> categories, Pageable pageable);

    //모든 채팅방 검색어만 있는 경우
    Page<Chatroom> findAllByNameContaining(String keyword,Pageable pageable);

    //모든 채팅방에 둘다 있는 경우
    Page<Chatroom> findAllByCategoryInAndNameContaining(Set<Category> categories, String keyword,Pageable pageable);

    /**
     * 우리 모임 - 내가 속해 있는 채팅방
     */
    // 내가 속해있는 채팅방 조회
    Page<Chatroom> findByMemberIdListContaining(Pageable pageable,Long userId);

    //내가 속해있는 채팅방에서 카테고리만 있는 경우
    Page<Chatroom> findByMemberIdListContainingAndCategoryIn(Pageable pageable,Long userId, Set<Category> categories);

    //내가 속해 있는 채팅방에서 카테고리 검색어 둘다 있는 경우
    Page<Chatroom> findByMemberIdListContainingAndCategoryInAndNameContaining(Pageable pageable,Long userId, Set<Category> categories, String keyword);

    //내가 속해 있는 채팅방에서 검색어만 있는 경우
    Page<Chatroom> findByMemberIdListContainingAndNameContaining(Pageable pageable,Long userId, String keyword);

    /**
     * 내모임 - 내가 방장인 채팅 방
     *
     */
    //내가 방장인 채팅방 조회
    Page<Chatroom> findByCreatorId(Pageable pageable,Long userId);

    //내가 방장인 방에서 카테고리만 있는 경우
    Page<Chatroom> findByCreatorIdAndCategoryIn(Pageable pageable,Long userId, Set<Category> categories);

    //내가 방장인 방에서 카테고리와 검색어 둘다 있는 경우
    Page<Chatroom> findByCreatorIdAndCategoryInAndNameContaining(Pageable pageable,Long userId, Set<Category> categories, String keyword);

    //내가 방장인 방에서 검색어만 있는 경우
    Page<Chatroom> findByCreatorIdAndNameContaining(Pageable pageable,Long userId, String keyword);

    Chatroom findByChatroomId(Long chatroomId);
}
