package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.*;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import com.up.and.down.chatroom.repository.ChatroomRepository;
import com.up.and.down.chatroom.entity.ChatroomMember;
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
public class ChatroomService {
    private final ChatroomRepository chatRoomRepository;
    private final MemberRepository memberRepository; // 닉네임 조회를 위해서 작성

    // 전체 목록 조회
    public Page<ChatroomListResponseDto> findAll(Pageable pageable, String nickname) {
        Page<Chatroom> chatrooms = chatRoomRepository.findAll(pageable);
        log.debug("chatrooms = {}", chatrooms.getContent());

        return chatrooms.map(chatroom -> {
            String creatorNickname = memberRepository.findById(chatroom.getCreatorId())
                    .map(Member::getNickname)
                    .orElse("Unknown");

            int participantCount = chatRoomRepository.countMembersByChatRoomId(chatroom.getChatroomId());
            return ChatroomListResponseDto.fromChatRoom(chatroom, creatorNickname, participantCount);
        });
    }

    // 검색
    private Page<Chatroom> search(Pageable pageable, String searchType, String keywords, Long memberId, Long creatorId) {
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
    public Page<ChatroomListResponseDto> findAllChatRooms(Pageable pageable, String searchType, String keywords, Set<Category> categories, String nickname) {
        Page<Chatroom> chatroom;

        if (categories != null && !categories.isEmpty()) {
            chatroom = chatRoomRepository.findByCategory(categories, pageable);
        } else {
            chatroom = search(pageable, searchType, keywords, null, null);
        }
        return chatroom.map(chatRoom -> {
            int participantCount = chatRoomRepository.countMembersByChatRoomId(chatRoom.getChatroomId());
            return ChatroomListResponseDto.builder()
                    .chatroomId(chatRoom.getChatroomId())
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
    public Page<ChatroomListResponseDto> findOurChatRooms(Long memberId, Pageable pageable, String searchType, String keywords, Set<Category> categories) {
        Page<Chatroom> chatroom;

        if (categories != null && !categories.isEmpty()) {
            chatroom = chatRoomRepository.findByCategoryAndMember(categories, memberId, pageable);
        } else {
            chatroom = search(pageable, searchType, keywords, memberId, null);
        }
        return chatroom.map(this::dto);
    }

    // 내모임
    public Page<ChatroomListResponseDto> findMyChatRooms(Long memberId, Pageable pageable, String searchType, String keywords, Set<Category> categories) {
        Page<Chatroom> chatroom;

        if (categories != null && !categories.isEmpty()) {
            chatroom = chatRoomRepository.findByCategoryCreator(categories, memberId, pageable);
        } else {
            chatroom = search(pageable,searchType, keywords, memberId, null);
        }
        return chatroom.map(this::dto);
    }

    // 채팅방 인원수 가져오기
    public int getChatRoomMemberCount(Long chatroomId) {
        Chatroom chatRoom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        return chatRoom.getMemberIdList().size(); // 채팅방에 속한 멤버 수 계산
    }

    // 채팅방 상세페이지 및 채팅 페이지에 멤버수 보여주기
    public ChatroomInfoDto getChatRoomInfoById(Long chatroomId) {
        log.debug("chatroomId = {}", chatroomId);
        Chatroom chatroom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        return ChatroomInfoDto.builder()
                .name(chatroom.getName())
                .categories(chatroom.getCategory())
                .memberCount(chatroom.getMemberIdList().size())
                .build();
    }

    private ChatroomListResponseDto dto(Chatroom chatroom) {
        return ChatroomListResponseDto.builder()
                .chatroomId(chatroom.getChatroomId())
                .name(chatroom.getName())
                .category(chatroom.getCategory())
                .description(chatroom.getDescription())
                .creatorId(chatroom.getCreatorId())
                .chatroomMember(chatroom.getMemberIdList().stream()
                        .map(ChatroomMember::new)
                        .collect(Collectors.toSet()))
                .createdAt(chatroom.getCreatedAt())
                .build();
    }

    // 상세페이지
    public ChatroomResponseDto findByChatRoom(Long chatroomId, Long memberId) {
        Chatroom chatroom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        String nickname = memberRepository.findById(chatroom.getCreatorId())
                .map(Member::getNickname)
                .orElse("Unknown");

        // 채팅방 멤버 수 계산
        int memberCount = chatroom.getMemberIdList().size();

        return ChatroomResponseDto.fromChatroom(chatroom, nickname, memberId);
    }

    // 채팅방 등록 페이지
    public void registChatRoom(ChatroomRegistRequestDto dto, Long creatorId) {
        Chatroom chatroom = dto.toChatroom(creatorId);
        log.debug("saved chatroom = {}", chatroom);
        chatRoomRepository.save(chatroom);
    }

    // 채팅방 소개글 수정페이지
    public ChatroomDetailResponseDto findById(Long chatroomId) {
        return ChatroomDetailResponseDto.fromChatroom(chatRoomRepository.findById(chatroomId).orElseThrow());
    }
    public void update(ChatroomUpdateRequestDto dto) {
        Chatroom chatroom = chatRoomRepository.findById(dto.getChatroomId()).orElseThrow();
        chatroom.update(dto); // 채팅방 정보 업데이트
        chatRoomRepository.save(chatroom); // 업데이트된 정보 저장
    }

    // 채팅방에 입장
    public void addMemberToChatRoom(Long chatroomId, Long memberId) {
        // 채팅방 조회
        Chatroom chatroom = chatRoomRepository.findById(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        // 채팅방 멤버 리스트에 새로운 멤버 추가
//        if (chatroom.getMemberIdList().contains(memberId)) {
//            log.debug("이미 존재하는 memberId = {}", memberId);
//        } else {
//            chatroom.getMemberIdList().add(memberId);
//            chatRoomRepository.save(chatroom);
//            log.debug("채팅방{}에 멤버{} 추가", chatroomId, memberId);
//        }
        chatroom.getMemberIdList().add(memberId);
        // 변경된 내용 db에 저장
        chatRoomRepository.save(chatroom);
        log.debug("추가된 멤버id = {}, 해당 채팅방 = {}", memberId, chatroomId);
    }
}