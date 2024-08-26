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
    public Page<ShowChatroomDto> findAllChatRooms(Pageable pageable) {
        Page<Chatroom> chatRoomList = chatroomRepository.findAll(pageable);

        // 3. 변환된 ShowChatRoomDto 리스트를 반환합니다.
        return chatRoomList
                .map(chatroom -> {
                    ShowChatroomDto dto = new ShowChatroomDto().toDto(chatroom);

                    memberService.findById(chatroom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                });
    }

    // 전체 채팅방 조회(탭 눌렀을 때) - 비동기
    public List<ShowChatroomDto> findAll() {
        List<Chatroom> list;
        list = chatroomRepository.findAll();
        return toShowChatroomDtoList(list);

    }

    // 전체 채팅방 카테고리별 조회 - 비동기
    public List<ShowChatroomDto> findAllByAsync(Set<Category> categories, String keyword) {

        List<Chatroom> list ;

        //둘다 값이 존재하는 경우 출력
        if (categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()) {
            log.debug("둘다 값이 존재하는 경우 출력");
            list = chatroomRepository.findAllByCategoryInAndNameContaining(categories,keyword);
        }
        //카테고리만 선택된 경우
        else if (categories != null && !categories.isEmpty()) {
            log.debug("카테고리만 선택된 경우");
            list = chatroomRepository.findAllByCategoryIn(categories);
        }
        // 검색어만 존재하는 경우
        else if (keyword != null && !keyword.isEmpty()) {
            log.debug("검색어만 존재하는 경우 ");
            list = chatroomRepository.findAllByNameContaining(keyword);
        }
        //둘다 존재하지 않을 경우
        else {
            log.debug("둘다 존재하지 않을 경우");
            list = chatroomRepository.findAll();
        }
        return toShowChatroomDtoList(list);
    }

    //우리 채팅방 탭 클릭했을 때 내가 속한 채팅방 리스트 출력하기 - 비동기
    public List<ShowChatroomDto> findOurChatRoomList(Long userId) {
        List<Chatroom> list = chatroomRepository.findByMemberIdListContaining(userId);
        return toShowChatroomDtoList(list);
    }

    //우리 채팅방에서 카테고리를 선택했을 경우 - 비동기
    public List<ShowChatroomDto> findAllOurChatRoom(Set<Category> categories, String keyword, Long userId) {

        List<Chatroom> list;

        // 카테고리와 검색어 둘 다 존재할 경우
        if(categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()){
            list = chatroomRepository.findByMemberIdListContainingAndCategoryInAndNameContaining(userId, categories, keyword);
        }
        // 카테고리만 존재할 경우
        else if(categories != null && !categories.isEmpty()) {
            list = chatroomRepository.findByMemberIdListContainingAndCategoryIn(userId, categories);
        }
        // 검색어만 존재할 경우
        else if(keyword != null && !keyword.isEmpty()) {
            list = chatroomRepository.findByMemberIdListContainingAndNameContaining(userId, keyword);
        }
        // 카테고리와 검색어 둘 다 없을 경우
        else{
            list = chatroomRepository.findByMemberIdListContaining(userId);
        }

        return toShowChatroomDtoList(list);
    }

    //내모임 탭 클릭 - 비동기
    public List<ShowChatroomDto> findAllMineChatRoom(Set<Category> categories, String keyword, Long userId) {

        List<Chatroom> list;

        // 카테고리와 검색어 둘 다 존재할 경우
        if(categories != null && !categories.isEmpty() && keyword != null && !keyword.isEmpty()){
            list = chatroomRepository.findByCreatorIdAndCategoryInAndNameContaining(userId, categories, keyword);
        }
        // 카테고리만 존재할 경우
        else if(categories != null && !categories.isEmpty()) {
            list = chatroomRepository.findByCreatorIdAndCategoryIn(userId, categories);
        }
        // 검색어만 존재할 경우
        else if(keyword != null && !keyword.isEmpty()) {
            list = chatroomRepository.findByCreatorIdAndNameContaining(userId, keyword);
        }
        // 카테고리와 검색어 둘 다 없을 경우
        else{
            list = chatroomRepository.findByCreatorId(userId);
        }

        //카테고리가 선택된 경우
        if (categories != null && !categories.isEmpty()) {

        }
        //카테고리가 선택되지 않은 경우
        else {

        }

        return toShowChatroomDtoList(list);
    }

    // 내모임 탭 카테고리별  - 비동기 ,
    public List<ShowChatroomDto> findByCreatorIdAndCategoryIn(Long userId, Set<Category> categories) {

        List<Chatroom> list = chatroomRepository.findByCreatorIdAndCategoryIn(userId, categories);
        return toShowChatroomDtoList(list);
    }

    //현선이 코드
    public ChatroomInfoDto getChatRoomInfo(Long chatRoomId) {
        Chatroom chatRoom = chatroomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        // 카테고리
        Set<Category> categories = chatRoom.getCategory();

        // 채팅방에 속한 멤버 수 계산
        int memberCount = chatRoom.getMemberIdList().size();

        return new ChatroomInfoDto(chatRoom.getName(), categories, memberCount);
    }

    private List<ShowChatroomDto> toShowChatroomDtoList(List<Chatroom> list) {
        return list.stream()
                .map(chatroom -> {
                    ShowChatroomDto dto = new ShowChatroomDto().toDto(chatroom);

                    memberService.findById(chatroom.getCreatorId()).ifPresent(member -> {
                        dto.setNickName(member.getNickname());
                    });
                    return dto;
                })
                .collect(Collectors.toList());
    }
}