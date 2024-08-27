package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.*;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import com.up.and.down.chatroom.repository.ChatroomRepository;
import com.up.and.down.user.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;
    private final MemberService memberService; // 닉네임 조회를 위해서 작성

    // 모든 채팅방 조회하는 리스트 - 동기
    public Page<ShowChatroomDto> findAllChatroom(Pageable pageable) {
        Page<Chatroom> chatRoomList = chatroomRepository.findAll(pageable);

        Page<ShowChatroomDto> showChatRoomDtoList = chatRoomList
                .map(chatRoom -> {
                    ShowChatroomDto dto = new ShowChatroomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                });


        // 3. 변환된 ShowChatroomDto 리스트를 반환합니다.
        return showChatRoomDtoList;
    }

    // 전체 채팅방 조회(탭 눌렀을 때) - 비동기
    public List<ShowChatroomDto> findAll() {
        List<Chatroom> list;
        list = chatroomRepository.findAll();

        List<ShowChatroomDto> showChatRoomDtoList = list.stream()
                .map(chatRoom -> {
                    ShowChatroomDto dto = new ShowChatroomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());

        return showChatRoomDtoList;
    }
    // 전체 채팅방 카테고리별 조회 - 비동기
    public Page<ShowChatroomDto> findAllByAsync(Pageable pageable, Set<Category> categories, String keyword) {

        Page<Chatroom> list ;

        //둘다 값이 존재하는 경우 출력
        if (categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()) {
            log.debug("둘다 값이 존재하는 경우 출력");
            list = chatroomRepository.findAllByCategoryInAndNameContaining(categories,keyword,pageable);
        }
        //카테고리만 선택된 경우
        else if (categories != null && !categories.isEmpty()) {
            log.debug("카테고리만 선택된 경우");
            list = chatroomRepository.findAllByCategoryIn(categories,pageable);
        }
        // 검색어만 존재하는 경우
        else if (keyword != null && !keyword.isEmpty()) {
            log.debug("검색어만 존재하는 경우 ");
            list = chatroomRepository.findAllByNameContaining(keyword,pageable);
        }
        //둘다 존재하지 않을 경우
        else {
            log.debug("둘다 존재하지 않을 경우");
            list = chatroomRepository.findAll(pageable);
        }

        List<ShowChatroomDto> dtoList = list.stream()
                .map(chatRoom -> {
                    ShowChatroomDto dto = new ShowChatroomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());

        Page<ShowChatroomDto> showChatRoomDtoList = new PageImpl<>(dtoList, pageable, list.getTotalElements());

        return showChatRoomDtoList;
    }

    //우리 채팅방에서 카테고리를 선택했을 경우 - 비동기
    public Page<ShowChatroomDto> findAllOurChatroom(Pageable pageable, Set<Category> categories, String keyword, Long userId) {

        Page<Chatroom> list ;

        // 카테고리와 검색어 둘 다 존재할 경우
        if(categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()){
            list = chatroomRepository.findByMemberIdListContainingAndCategoryInAndNameContaining(pageable,userId, categories, keyword);
        }
        // 카테고리만 존재할 경우
        else if(categories != null && !categories.isEmpty()) {
            list = chatroomRepository.findByMemberIdListContainingAndCategoryIn(pageable,userId, categories);
        }
        // 검색어만 존재할 경우
        else if(keyword != null && !keyword.isEmpty()) {
            list = chatroomRepository.findByMemberIdListContainingAndNameContaining(pageable,userId, keyword);
        }
        // 카테고리와 검색어 둘 다 없을 경우
        else{
            list = chatroomRepository.findByMemberIdListContaining(pageable,userId);
            log.debug("카테고리와 검색이 둘다 없는경우 ");
            log.debug("리스트 출력드려용{}", list.toString());
        }

        List<ShowChatroomDto> dtoList = list.stream()
                .map(chatRoom -> {
                    ShowChatroomDto dto = new ShowChatroomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());


        Page<ShowChatroomDto> showChatRoomDtoList = new PageImpl<>(dtoList, pageable, list.getTotalElements());
        return showChatRoomDtoList;
    }

    // 내모임 탭 클릭 - 비동기
    public Page<ShowChatroomDto> findAllMineChatroom(Pageable pageable, Set<Category> categories, String keyword, Long userId) {

        Page<Chatroom> list;

        // 카테고리와 검색어 둘 다 존재할 경우
        if(categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()){
            list = chatroomRepository.findByCreatorIdAndCategoryInAndNameContaining(pageable,userId, categories, keyword);
        }
        // 카테고리만 존재할 경우
        else if(categories != null && !categories.isEmpty()) {
            list = chatroomRepository.findByCreatorIdAndCategoryIn(pageable,userId, categories);
        }
        // 검색어만 존재할 경우
        else if(keyword != null && !keyword.isEmpty()) {
            list = chatroomRepository.findByCreatorIdAndNameContaining(pageable,userId, keyword);
        }
        // 카테고리와 검색어 둘 다 없을 경우
        else{
            list = chatroomRepository.findByCreatorId(pageable,userId);
        }


        List<ShowChatroomDto> mineList = list.stream()
                .map(chatRoom -> {
                    ShowChatroomDto dto = new ShowChatroomDto().toDto(chatRoom);

                    memberService.findById(chatRoom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());

        Page<ShowChatroomDto> showChatroomDtoList = new PageImpl<>(mineList, pageable, list.getTotalElements());
        return showChatroomDtoList;
    }

    // 채팅방 상세페이지 및 채팅 페이지에 멤버수 보여주기
    public ChatroomInfoDto getChatroomInfoById(Long chatroomId) {
        log.debug("chatroomId = {}", chatroomId);
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        return ChatroomInfoDto.builder()
                .name(chatroom.getName())
                .categories(chatroom.getCategory())
                .memberCount(chatroom.getMemberIdList().size())
                .build();
    }

    // 채팅방에 입장
    public void addMemberToChatroom(Long chatroomId, Long memberId) {
        // 채팅방 조회
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        chatroom.getMemberIdList().add(memberId);
        // 변경된 내용 db에 저장
        chatroomRepository.save(chatroom);
        log.debug("추가된 멤버id = {}, 해당 채팅방 = {}", memberId, chatroomId);
    }

    // 채팅방 등록 페이지
    public void registChatroom(ChatroomRegistRequestDto dto, Long creatorId) {
        Chatroom chatroom = dto.toChatroom(creatorId);
        log.debug("saved chatroom = {}", chatroom);
        chatroomRepository.save(chatroom);
    }

    // 채팅방 소개글 수정페이지
    public ChatroomDetailResponseDto findById(Long chatroomId) {
        return ChatroomDetailResponseDto.fromChatroom(chatroomRepository.findById(chatroomId).orElseThrow());
    }

    public void update(ChatroomUpdateRequestDto dto) {
        Chatroom chatroom = chatroomRepository.findById(dto.getChatroomId()).orElseThrow();
        chatroom.update(dto); // 채팅방 정보 업데이트
        chatroomRepository.save(chatroom); // 업데이트된 정보 저장
    }

    // 채팅방에 입장
    public void addMemberToChatRoom(Long chatroomId, Long memberId) {
        // 채팅방 조회
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        chatroom.getMemberIdList().add(memberId);
        // 변경된 내용 db에 저장
        chatroomRepository.save(chatroom);
        log.debug("추가된 멤버id = {}, 해당 채팅방 = {}", memberId, chatroomId);
    }

    public ChatroomDto findByChatroomId(Long chatroomId) {
        ChatroomDto chatroomDto = this.chatroomRepository.findByChatroomId(chatroomId).toChatroomDto();
        chatroomDto.setCreatorNickname(this.memberService.findById(chatroomDto.getCreatorId()).orElseThrow().getNickname());
        return chatroomDto;
    }
}