package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.ChatRoomListResponseDto;
import com.up.and.down.chatroom.dto.ChatRoomResponseDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.repository.ChatRoomRepository;
import com.up.and.down.chatroom.entity.ChatRoomMember;
import com.up.and.down.user.member.entity.Member;
import com.up.and.down.user.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    // 전체 목록 조회
    public Page<ChatRoomListResponseDto> findAll(Pageable pageable, String nickname) {
        Page<ChatRoom> chatRooms = chatRoomRepository.findAll(pageable);
        log.debug("chatRooms = {}", chatRooms.getContent());

        return chatRooms.map(chatRoom -> {
            int participantCount = chatRoomRepository.countMembersByChatRoomId(chatRoom.getChatRoomId());
            return ChatRoomListResponseDto.fromChatRoom(chatRoom, nickname, participantCount);
        });
    }

    // 검색
    private Page<ChatRoom> search(Pageable pageable, String searchType, String q, Long memberId, Long creatorId) {
        // 제목으로 검색할 경우
        if ("name".equalsIgnoreCase(searchType)) {
            if (memberId != null) {
                // member가 속한 채팅방 제목으로 검색
                return chatRoomRepository.findByNameContainingAndMember(q, memberId, pageable);
            } else if (creatorId != null) {
                // member가 만든 채팅방 제목으로 검색
                return chatRoomRepository.findByNameContainingAndCreator(q, creatorId, pageable);
            } else {
                // 전체 탭에서 제목 검색
                return chatRoomRepository.findByNameContaining(q, pageable);
            }
            // 제목+내용으로 검색할 경우
        } else if ("nameAndDescription".equalsIgnoreCase(searchType)) {
            if (memberId != null) {
                // member가 속한 채팅방 제목+내용으로 검색
                return chatRoomRepository.findByNameContainingOrDescriptionContainingAndMemberId(q, q, memberId, pageable);
            } else if (creatorId != null) {
                // member가 만든 채팅방 제목+내용으로 검색
                return chatRoomRepository.findByNameContainingOrDescriptionContainingAndCreatorId(q, q, creatorId, pageable);
            } else {
                // 전체 탭에서 제목+내용 검색
                return chatRoomRepository.findByNameContainingOrDescriptionContaining(q, q, pageable);
            }
        } else {
            return Page.empty(pageable);
        }
    }

    // 전체
    public Page<ChatRoomListResponseDto> findAllChatRooms(Pageable pageable, String searchType, String q, Set<Category> categories, String nickname) {
        Page<ChatRoom> chatRooms;

        if (categories != null && !categories.isEmpty()) {
            chatRooms = chatRoomRepository.findByCategory(categories, pageable);
        } else {
            chatRooms = search(pageable, searchType, q, null, null);
        }
        return chatRooms.map(chatRoom -> {
            int participantCount = chatRoomRepository.countMembersByChatRoomId(chatRoom.getChatRoomId());
            return ChatRoomListResponseDto.builder()
                    .chatRoomId(chatRoom.getChatRoomId())
                    .name(chatRoom.getName())
                    .category(chatRoom.getCategory())
                    .description(chatRoom.getDescription())
                    .creatorId(chatRoom.getCreatorId())
                    .memberCount(participantCount)
                    .nickname(nickname) // 생성자 닉네임 추가
                    .createdAt(chatRoom.getCreatedAt())
                    .build();
        });
    }

    // 우리모임
    public Page<ChatRoomListResponseDto> findOurChatRooms(Long memberId, Pageable pageable, String searchType, String q, Set<Category> categories) {
        Page<ChatRoom> chatRooms;

        if (categories != null && !categories.isEmpty()) {
            chatRooms = chatRoomRepository.findByCategoryAndMember(categories, memberId, pageable);
        } else {
            chatRooms = search(pageable, searchType, q, memberId, null);
        }
        return chatRooms.map(this::dto);
    }

    // 내모임
    public Page<ChatRoomListResponseDto> findMyChatRooms(Long memberId, Pageable pageable, String searchType, String q, Set<Category> categories) {
        Page<ChatRoom> chatRooms;

        if (categories != null && !categories.isEmpty()) {
            chatRooms = chatRoomRepository.findByCategoryCreator(categories, memberId, pageable);
        } else {
            chatRooms = search(pageable,searchType, q, memberId, null);
        }
        return chatRooms.map(this::dto);
    }

    // 채팅방 인원수
    public int getChatRoomMemberCount(Long chatRoomId) {
        return chatRoomRepository.countMembersByChatRoomId(chatRoomId);
    }

    private ChatRoomListResponseDto dto(ChatRoom chatRoom) {
        return ChatRoomListResponseDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .name(chatRoom.getName())
                .category(chatRoom.getCategory())
                .description(chatRoom.getDescription())
                .creatorId(chatRoom.getCreatorId())
                .chatRoomMember(chatRoom.getMemberIdList().stream()
                        .map(ChatRoomMember::new)
                        .collect(Collectors.toSet()))
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
