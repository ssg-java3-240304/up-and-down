package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.ChatRoomListResponseDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.repository.ChatRoomRepository;
import com.up.and.down.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    // 검색
    public Page<ChatRoomListResponseDto> searchChatRoom(String keyword, Set<Category> category, Pageable pageable) {
        Page<ChatRoom> chatRoomPage;
        if (keyword != null && category != null) {
            // 제목 + 내용 + 카테고리
            chatRoomPage = chatRoomRepository.searchByNameAndContentAndCategory(keyword, category, pageable);
        } else if (keyword != null) {
            // 제목, 제목 + 내용
            chatRoomPage = chatRoomRepository.searchByNameAndContent(keyword, pageable);
        } else if (category != null) {
            // 카테고리
            chatRoomPage = chatRoomRepository.searchCategory(category, pageable);
        } else  {
            // 전체
            chatRoomPage = chatRoomRepository.findAll(pageable);
        }
        return chatRoomPage.map(ChatRoomListResponseDto::fromChatRoom);
    }

    // 목록 조회
    public Page<ChatRoomListResponseDto> getChatRoom(String filter, User user, Pageable pageable) {
        if (filter == null) {
            filter = "all";
        }
        if (user == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다");
        }
        Page<ChatRoom> chatRoomPage = switch (filter) {
            case "all" ->
                // 전체 탭
                    chatRoomRepository.findAll(pageable);
            case "our" ->
                // 우리모임 탭
                    chatRoomRepository.findChatRoomByMemberId(user, pageable);
            default ->
                // 내모임 탭
                    chatRoomRepository.findChatRoomCreatedByMemberId(user, pageable);
        };
        return chatRoomPage.map(ChatRoomListResponseDto::fromChatRoom);
    }
}
