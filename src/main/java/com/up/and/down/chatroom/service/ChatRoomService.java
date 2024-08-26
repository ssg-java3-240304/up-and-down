package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.*;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.repository.ChatRoomRepository;
import com.up.and.down.user.member.entity.Member;
import com.up.and.down.user.member.repository.MemberRepository;
import com.up.and.down.user.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService; // 닉네임 조회를 위해서 작성

    // 모든 채팅방 조회하는 리스트 - 동기
    public Page<ShowChatRoomDto> findAllChatRooms(Pageable pageable) {
        Page<ChatRoom> chatRoomList = chatRoomRepository.findAll(pageable);

        Page<ShowChatRoomDto> showChatRoomDtoList = chatRoomList
                .map(chatRoom -> {
                    ShowChatRoomDto dto = new ShowChatRoomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                });


        // 3. 변환된 ShowChatRoomDto 리스트를 반환합니다.
        return showChatRoomDtoList;
    }
    // 전체 채팅방 조회(탭 눌렀을 때) - 비동기
    public List<ShowChatRoomDto> findAll() {
        List<ChatRoom> list;
        list = chatRoomRepository.findAll();

        List<ShowChatRoomDto> showChatRoomDtoList = list.stream()
                .map(chatRoom -> {
                    ShowChatRoomDto dto = new ShowChatRoomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());

        return showChatRoomDtoList;
    }
    // 전체 채팅방 카테고리별 조회 - 비동기
    public Page<ShowChatRoomDto> findAllByAsync(Pageable pageable,Set<Category> categories,String keyword) {

        Page<ChatRoom> list ;

        //둘다 값이 존재하는 경우 출력
        if (categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()) {
            log.debug("둘다 값이 존재하는 경우 출력");
            list = chatRoomRepository.findAllByCategoryInAndNameContaining(categories,keyword,pageable);
        }
        //카테고리만 선택된 경우
        else if (categories != null && !categories.isEmpty()) {
            log.debug("카테고리만 선택된 경우");
            list = chatRoomRepository.findAllByCategoryIn(categories,pageable);
        }
        // 검색어만 존재하는 경우
        else if (keyword != null && !keyword.isEmpty()) {
            log.debug("검색어만 존재하는 경우 ");
            list = chatRoomRepository.findAllByNameContaining(keyword,pageable);
        }
        //둘다 존재하지 않을 경우
        else {
            log.debug("둘다 존재하지 않을 경우");
            list = chatRoomRepository.findAll(pageable);
        }

        List<ShowChatRoomDto> dtoList = list.stream()
                .map(chatRoom -> {
                    ShowChatRoomDto dto = new ShowChatRoomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());

        Page<ShowChatRoomDto> showChatRoomDtoList = new PageImpl<>(dtoList, pageable, list.getTotalElements());

        return showChatRoomDtoList;
    }

//    //우리 채팅방 탭 클릭했을 때 내가 속한 채팅방 리스트 출력하기 - 비동기
//    public List<ShowChatRoomDto> findOurChatRoomList(Long userId) {
//        List<ChatRoom> list = chatRoomRepository.findByMemberIdListContaining(userId);
//
//        List<ShowChatRoomDto> showChatRoomDtoList = list.stream()
//                .map(chatRoom -> {
//                    ShowChatRoomDto dto = new ShowChatRoomDto().toDto(chatRoom);
//
//                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
//                        dto.setNickName(member.getNickname());
//                    });
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        return showChatRoomDtoList;
//    }

    //우리 채팅방에서 카테고리를 선택했을 경우 - 비동기
    public Page<ShowChatRoomDto> findAllOurChatRoom(Pageable pageable,Set<Category> categories, String keyword, Long userId) {

        Page<ChatRoom> list ;

        // 카테고리와 검색어 둘 다 존재할 경우
        if(categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()){
            list = chatRoomRepository.findByMemberIdListContainingAndCategoryInAndNameContaining(pageable,userId, categories, keyword);
        }
        // 카테고리만 존재할 경우
        else if(categories != null && !categories.isEmpty()) {
            list = chatRoomRepository.findByMemberIdListContainingAndCategoryIn(pageable,userId, categories);
        }
        // 검색어만 존재할 경우
        else if(keyword != null && !keyword.isEmpty()) {
            list = chatRoomRepository.findByMemberIdListContainingAndNameContaining(pageable,userId, keyword);
        }
        // 카테고리와 검색어 둘 다 없을 경우
        else{
            list = chatRoomRepository.findByMemberIdListContaining(pageable,userId);
            log.debug("카테고리와 검색이 둘다 없는경우 ");
            log.debug("리스트 출력드려용{}", list.toString());
        }

        List<ShowChatRoomDto> dtoList = list.stream()
                .map(chatRoom -> {
                    ShowChatRoomDto dto = new ShowChatRoomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());


        Page<ShowChatRoomDto> showChatRoomDtoList = new PageImpl<>(dtoList, pageable, list.getTotalElements());
        return showChatRoomDtoList;
    }

   // 내모임 탭 클릭 - 비동기
    public Page<ShowChatRoomDto> findAllMineChatRoom(Pageable pageable,Set<Category> categories,String keyword, Long userId) {

        Page<ChatRoom> list;

        // 카테고리와 검색어 둘 다 존재할 경우
        if(categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()){
            list = chatRoomRepository.findByCreatorIdAndCategoryInAndNameContaining(pageable,userId, categories, keyword);
        }
        // 카테고리만 존재할 경우
        else if(categories != null && !categories.isEmpty()) {
            list = chatRoomRepository.findByCreatorIdAndCategoryIn(pageable,userId, categories);
        }
        // 검색어만 존재할 경우
        else if(keyword != null && !keyword.isEmpty()) {
            list = chatRoomRepository.findByCreatorIdAndNameContaining(pageable,userId, keyword);
        }
        // 카테고리와 검색어 둘 다 없을 경우
        else{
            list = chatRoomRepository.findByCreatorId(pageable,userId);
        }


        List<ShowChatRoomDto> mineList = list.stream()
                .map(chatRoom -> {
                    ShowChatRoomDto dto = new ShowChatRoomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());

        Page<ShowChatRoomDto> showChatRoomDtoList = new PageImpl<>(mineList, pageable, list.getTotalElements());
        return showChatRoomDtoList;
    }

//    // 내모임 탭 카테고리별  - 비동기 ,
//    public Page<ShowChatRoomDto> findByCreatorIdAndCategoryIn(Long userId, Set<Category> categories) {
//
//        Page<ChatRoom> list ;
//
//        List<ShowChatRoomDto> mineList = list.stream()
//                .map(chatRoom -> {
//                    ShowChatRoomDto dto = new ShowChatRoomDto().toDto(chatRoom);
//
//                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
//                        dto.setNickName(member.getNickname());
//                    });
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        Page<ShowChatRoomDto> showChatRoomDtoList = new PageImpl<>(mineList, pageable, list.getTotalElements());
//        return mineList;
//    }




    //현선이 코드
    public ChatRoomInfoDto getChatRoomInfo(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        // 카테고리
        Set<Category> categories = chatRoom.getCategory();

        // 채팅방에 속한 멤버 수 계산
        int memberCount = chatRoom.getMemberIdList().size();

        return new ChatRoomInfoDto(chatRoom.getName(), categories, memberCount);
    }





}
// 전체 목록 조회

//    public Page<ChatRoomListResponseDto> findAll(Pageable pageable, String nickname) {
//        Page<ChatRoom> chatRooms = chatRoomRepository.findAll(pageable);
//        log.debug("chatRooms = {}", chatRooms.getContent());
//
//        return chatRooms.map(chatRoom -> {
//            String creatorNickname = memberService.findById(chatRoom.getCreatorId())
//                    .map(Member::getNickname)
//                    .orElse("Unknown");
//
//            int participantCount = chatRoomRepository.countMembersByChatRoomId(chatRoom.getChatRoomId());
//            return ChatRoomListResponseDto.fromChatRoom(chatRoom, creatorNickname, participantCount);
//        });
//    }


//
//    public Page<ChatRoom> search(Pageable pageable, String searchType, String keywords, Long memberId, Long creatorId) {
//        // 제목으로 검색할 경우
//        if ("name".equalsIgnoreCase(searchType)) {
//            if (memberId != null) {
//                // member가 속한 채팅방 제목으로 검색
//                return chatRoomRepository.findByNameContainingAndMember(keywords, memberId, pageable);
//            } else if (creatorId != null) {
//                // member가 만든 채팅방 제목으로 검색
//                return chatRoomRepository.findByNameContainingAndCreator(keywords, creatorId, pageable);
//            } else {
//                // 전체 탭에서 제목 검색
//                return chatRoomRepository.findByNameContaining(keywords, pageable);
//            }
//            // 제목+내용으로 검색할 경우
//        } else if ("nameAndDescription".equalsIgnoreCase(searchType)) {
//            if (memberId != null) {
//                // member가 속한 채팅방 제목+내용으로 검색
//                return chatRoomRepository.findByNameContainingOrDescriptionContainingAndMemberId(keywords, keywords, memberId, pageable);
//            } else if (creatorId != null) {
//                // member가 만든 채팅방 제목+내용으로 검색
//                return chatRoomRepository.findByNameContainingOrDescriptionContainingAndCreatorId(keywords, keywords, creatorId, pageable);
//            } else {
//                // 전체 탭에서 제목+내용 검색
//                return chatRoomRepository.findByNameContainingOrDescriptionContaining(keywords, keywords, pageable);
//            }
//        } else {
//            return Page.empty(pageable);
//        }
//    }
//    // 전체
//
////    public Page<ChatRoomListResponseDto> findAllChatRooms(Pageable pageable, String searchType, String keywords, Set<Category> categories, String nickname) {
////        Page<ChatRoom> chatRooms;
////
////        if (categories != null && !categories.isEmpty()) {
////            chatRooms = chatRoomRepository.findByCategory(categories, pageable);
////        } else {
////            chatRooms = search(pageable, searchType, keywords, null, null);
////        }
////        return chatRooms.map(chatRoom -> {
////            int participantCount = chatRoomRepository.countMembersByChatRoomId(chatRoom.getChatRoomId());
////            return ChatRoomListResponseDto.builder()
////                    .chatRoomId(chatRoom.getChatRoomId())
////                    .name(chatRoom.getName())
////                    .category(chatRoom.getCategory())
////                    .description(chatRoom.getDescription())
////                    .creatorId(chatRoom.getCreatorId())
////                    .memberCount(participantCount)
////                    .nickname(nickname) // 생성자 닉네임 추가
////                    .createdAt(chatRoom.getCreatedAt())
////                    .build();
////        });
////    }
//    // 우리모임
////    public Page<ChatRoomListResponseDto> findOurChatRooms(Long memberId, Pageable pageable, String searchType, String keywords, Set<Category> categories) {
////        Page<ChatRoom> chatRooms;
////
////        if (categories != null && !categories.isEmpty()) {
//
////            chatRooms = chatRoomRepository.findByCategoryAndMember(categories, memberId, pageable);
////        } else {
////            chatRooms = search(pageable, searchType, keywords, memberId, null);
////        }
//
////        return chatRooms.map(this::dto);
////    }
////    // 내모임
////    public Page<ChatRoomListResponseDto> findMyChatRooms(Long memberId, Pageable pageable, String searchType, String keywords, Set<Category> categories) {
////        Page<ChatRoom> chatRooms;
////
////        if (categories != null && !categories.isEmpty()) {
////            chatRooms = chatRoomRepository.findByCategoryCreator(categories, memberId, pageable);
////        } else {
////            chatRooms = search(pageable,searchType, keywords, memberId, null);
////        }
//
////        return chatRooms.map(this::dto);
////    }
//
//    // 채팅방 인원수
//    public int getChatRoomMemberCount(Long chatRoomId) {
//        return chatRoomRepository.countMembersByChatRoomId(chatRoomId);
//    }
////    private ChatRoomListResponseDto dto(ChatRoom chatRoom) {
////        return ChatRoomListResponseDto.builder()
////                .chatRoomId(chatRoom.getChatRoomId())
////                .name(chatRoom.getName())
////                .category(chatRoom.getCategory())
////                .description(chatRoom.getDescription())
////                .creatorId(chatRoom.getCreatorId())
////                .chatRoomMember(chatRoom.getMemberIdList().stream()
////                        .map(ChatRoomMember::new)
////                        .collect(Collectors.toSet()))
//
////                .createdAt(chatRoom.getCreatedAt())
////                .build();
////    }
//
//    // 상세페이지
//
//    public ChatRoomResponseDto findByChatRoom(Long chatRoomId, Long memberId) {
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
//                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
//
//        String nickname = memberService.findById(chatRoom.getCreatorId())
//                .map(Member::getNickname)
//                .orElse("Unknown");
//
//        // 채팅방 멤버 수 계산
//        int memberCount = chatRoom.getMemberIdList().size();
//
//        return ChatRoomResponseDto.fromChatRoom(chatRoom, nickname, memberId);
//    }
//    // 채팅방에서 보여줘야 하는 데이터(채팅방 이름, 카테고리, 참여인원수)
//
//
//
//    // 채팅방 등록 페이지
//
//    public void registChatRoom(ChatRoomRegistRequestDto dto, Long creatorId) {
//        ChatRoom chatRoom = dto.toChatRoom(creatorId);
//        log.debug("saved chatRoom = {}", chatRoom);
//        chatRoomRepository.save(chatRoom);
//    }
//





