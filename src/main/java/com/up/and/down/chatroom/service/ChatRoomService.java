package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.*;
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
    private final MemberRepository memberRepository; // 닉네임 조회를 위해서 작성

    // 전체 목록 조회
    public Page<ChatRoomListResponseDto> findAll(Pageable pageable, String nickname) {
        Page<ChatRoom> chatRooms = chatRoomRepository.findAll(pageable);
        log.debug("chatRooms = {}", chatRooms.getContent());

        return chatRooms.map(chatRoom -> {
            String creatorNickname = memberRepository.findById(chatRoom.getCreatorId())
                    .map(Member::getNickname)
                    .orElse("Unknown");

            int participantCount = chatRoomRepository.countMembersByChatRoomId(chatRoom.getChatRoomId());
            return ChatRoomListResponseDto.fromChatRoom(chatRoom, creatorNickname, participantCount);
        });
    }

    // 검색
    private Page<ChatRoom> search(Pageable pageable, String searchType, String keywords, Long memberId, Long creatorId) {
        // 제목으로 검색할 경우
        if ("name".equalsIgnoreCase(searchType)) {
            if (memberId != null) {
                // member가 속한 채팅방 제목으로 검색
                return chatRoomRepository.findByNameContainingAndMember(keywords, memberId, pageable);
            } else if (creatorId != null) {
                // member가 만든 채팅방 제목으로 검색
                return chatRoomRepository.findByNameContainingAndCreator(keywords, creatorId, pageable);
            } else {
                // 전체 탭에서 제목 검색
                return chatRoomRepository.findByNameContaining(keywords, pageable);
            }
            // 제목+내용으로 검색할 경우
        } else if ("nameAndDescription".equalsIgnoreCase(searchType)) {
            if (memberId != null) {
                // member가 속한 채팅방 제목+내용으로 검색
                return chatRoomRepository.findByNameContainingOrDescriptionContainingAndMemberId(keywords, keywords, memberId, pageable);
            } else if (creatorId != null) {
                // member가 만든 채팅방 제목+내용으로 검색
                return chatRoomRepository.findByNameContainingOrDescriptionContainingAndCreatorId(keywords, keywords, creatorId, pageable);
            } else {
                // 전체 탭에서 제목+내용 검색
                return chatRoomRepository.findByNameContainingOrDescriptionContaining(keywords, keywords, pageable);
            }
        } else {
            return Page.empty(pageable);
        }
    }

    // 전체
    public Page<ChatRoomListResponseDto> findAllChatRooms(Pageable pageable, String searchType, String keywords, Set<Category> categories, String nickname) {
        Page<ChatRoom> chatRooms;

        if (categories != null && !categories.isEmpty()) {
            chatRooms = chatRoomRepository.findByCategory(categories, pageable);
        } else {
            chatRooms = search(pageable, searchType, keywords, null, null);
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
    public Page<ChatRoomListResponseDto> findOurChatRooms(Long memberId, Pageable pageable, String searchType, String keywords, Set<Category> categories) {
        Page<ChatRoom> chatRooms;

        if (categories != null && !categories.isEmpty()) {
            chatRooms = chatRoomRepository.findByCategoryAndMember(categories, memberId, pageable);
        } else {
            chatRooms = search(pageable, searchType, keywords, memberId, null);
        }
        return chatRooms.map(this::dto);
    }

    // 내모임
    public Page<ChatRoomListResponseDto> findMyChatRooms(Long memberId, Pageable pageable, String searchType, String keywords, Set<Category> categories) {
        Page<ChatRoom> chatRooms;

        if (categories != null && !categories.isEmpty()) {
            chatRooms = chatRoomRepository.findByCategoryCreator(categories, memberId, pageable);
        } else {
            chatRooms = search(pageable,searchType, keywords, memberId, null);
        }
        return chatRooms.map(this::dto);
    }

    // 채팅방 인원수 가져오기
    public int getChatRoomMemberCount(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        return chatRoom.getMemberIdList().size(); // 채팅방에 속한 멤버 수 계산
    }

    // 채팅방 상세페이지 및 채팅 페이지에 멤버수 보여주기
    public ChatRoomInfoDto getChatRoomInfo(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        // 카테고리
        Set<Category> categories = chatRoom.getCategory();

        // 채팅방에 속한 멤버 수
        int memberCount = chatRoomRepository.countMembersByChatRoomId(chatRoomId);

        return new ChatRoomInfoDto(chatRoom.getName(), categories, memberCount);
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

    // 상세페이지
    public ChatRoomResponseDto findByChatRoom(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        String nickname = memberRepository.findById(chatRoom.getCreatorId())
                .map(Member::getNickname)
                .orElse("Unknown");

        // 채팅방 멤버 수 계산
        int memberCount = chatRoom.getMemberIdList().size();

        return ChatRoomResponseDto.fromChatRoom(chatRoom, nickname, memberId);
    }

    // 채팅방 등록 페이지
    public void registChatRoom(ChatRoomRegistRequestDto dto, Long creatorId) {
        ChatRoom chatRoom = dto.toChatRoom(creatorId);
        log.debug("saved chatRoom = {}", chatRoom);
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 소개글 수정페이지
    public ChatRoomDetailResponseDto findById(Long chatRoomId) {
        return ChatRoomDetailResponseDto.fromChatRoom(chatRoomRepository.findById(chatRoomId).orElseThrow());
    }
    public void update(ChatRoomUpdateRequestDto dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId()).orElseThrow();
        chatRoom.update(dto); // 채팅방 정보 업데이트
        chatRoomRepository.save(chatRoom); // 업데이트된 정보 저장
    }

    // 채팅방에 입장
    public void addMemberToChatRoom(Long chatRoomId, Long memberId) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        // 채팅방 멤버 리스트에 새로운 멤버 추가
//        if (chatRoom.getMemberIdList().contains(memberId)) {
//            log.debug("이미 존재하는 memberId = {}", memberId);
//        } else {
//            chatRoom.getMemberIdList().add(memberId);
//            chatRoomRepository.save(chatRoom);
//            log.debug("채팅방{}에 멤버{} 추가", chatRoomId, memberId);
//        }
        chatRoom.getMemberIdList().add(memberId);
        // 변경된 내용 db에 저장
        chatRoomRepository.save(chatRoom);
        log.debug("추가된 멤버id = {}, 해당 채팅방 = {}", memberId, chatRoomId);
    }
}