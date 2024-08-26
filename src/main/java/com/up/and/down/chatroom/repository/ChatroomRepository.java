package com.up.and.down.chatroom.repository;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
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

    /**
     * 우리 모임 - 내가 속해 있는 채팅방
     */
    // 내가 속해있는 채팅방 조회
    List<Chatroom> findByMemberIdListContaining(Long userId);

    //내가 속해있는 채팅방에서 카테고리만 있는 경우
    List<Chatroom> findByMemberIdListContainingAndCategoryIn(Long userId, Set<Category> categories);

    //내가 속해 있는 채팅방에서 카테고리 검색어 둘다 있는 경우
    List<Chatroom> findByMemberIdListContainingAndCategoryInAndNameContaining(Long userId, Set<Category> categories, String keyword);

    //내가 속해 있는 채팅방에서 검색어만 있는 경우
    List<Chatroom> findByMemberIdListContainingAndNameContaining(Long userId, String keyword);

    /**
     * 내모임 - 내가 방장인 채팅 방
     *
     */
    //내가 방장인 채팅방 조회
    List<Chatroom> findByCreatorId(Long userId);

    //내가 방장인 방에서 카테고리만 있는 경우
    List<Chatroom> findByCreatorIdAndCategoryIn(Long userId, Set<Category> categories);

    //내가 방장인 방에서 카테고리와 검색어 둘다 있는 경우
    List<Chatroom> findByCreatorIdAndCategoryInAndNameContaining(Long userId, Set<Category> categories, String keyword);

    //내가 방장인 방에서 검색어만 있는 경우
    List<Chatroom> findByCreatorIdAndNameContaining(Long userId, String keyword);
}