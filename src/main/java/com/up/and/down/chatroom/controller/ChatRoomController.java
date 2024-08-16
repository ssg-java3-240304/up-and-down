package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatRoomListResponseDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.common.paging.PageCriteria;
import com.up.and.down.user.User;
import com.up.and.down.user.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // [커뮤니티] 메인페이지 보여주기
    @GetMapping("/list")
    public String list(@PageableDefault(page = 0, size = 10) Pageable pageable,
                       @AuthenticationPrincipal AuthPrincipal principal,
                       Model model){

        // 사용자 닉네임 가져오기
        String nickname = (principal != null && principal.getUser() instanceof Member) ?
                ((Member) principal.getUser()).getNickname() : "Guest";

        // 전체 채팅방 목록 가져오기
        Page<ChatRoomListResponseDto> chatRoomPage = chatRoomService.findAll(pageable, nickname);

        model.addAttribute("chatRooms", chatRoomPage.getContent());
        model.addAttribute("currentPage", chatRoomPage.getNumber());
        model.addAttribute("totalPages", chatRoomPage.getTotalPages());
        model.addAttribute("totalCount", chatRoomPage.getTotalElements());

        return "chatroom/list";
    }

    // 전체 채팅방 목록
    @GetMapping("")
    @ResponseBody
    public Page<ChatRoomListResponseDto> getAllChatRooms(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                         @RequestParam(required = false) String searchType, // 제목, 제목+내용
                                                         @RequestParam(required = false) String q, // 검색키워드
                                                         @RequestParam(required = false) Set<Category> categories,
                                                         @AuthenticationPrincipal AuthPrincipal principal){ // 로그인된 사용자 정보
        log.debug("GET /chat-rooms/all?page={}", pageable.getPageNumber());
        log.debug("GET /chat-rooms/all?page={}&searchType={}&q={}&categories={}", pageable.getPageNumber(), searchType, q, categories);

        // 로그인된 사용자 닉네임 가져오기
        String nickname = (principal != null && principal.getUser() instanceof Member) ?
                ((Member) principal.getUser()).getNickname() : "Guest";
        log.debug("nickname = {}", nickname);

        Page<ChatRoomListResponseDto> chatRoomPage = chatRoomService.findAllChatRooms(pageable, searchType, q, categories, nickname);
        log.debug("chatRoomPage = {}", chatRoomPage);

        return chatRoomPage;
    }
    // 우리모임 채팅방 목록
    @GetMapping("/our")
    @ResponseBody
    public Page<ChatRoomListResponseDto> getOurChatRooms(@AuthenticationPrincipal AuthPrincipal principal,
                                                         @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                         @RequestParam(required = false) String searchType, // 제목, 제목+내용
                                                         @RequestParam(required = false) String q, // 검색키워드
                                                         @RequestParam(required = false) Set<Category> categories){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        log.debug("GET /chat-rooms/our?page={}", pageable.getPageNumber());
        log.debug("GET /chat-rooms/our?page={}&searchType={}&q={}&categories={}", pageable.getPageNumber(), searchType, q, categories);

        Long memberId = principal.getUser().getId();
        Page<ChatRoomListResponseDto> chatRoomPage = chatRoomService.findOurChatRooms(memberId, pageable, searchType, q, categories);
        log.debug("chatRoomPage = {}", chatRoomPage);

        return chatRoomPage;
    }
    // 내모임 채팅방 목록
    @GetMapping("/my")
    @ResponseBody
    public Page<ChatRoomListResponseDto> getMyChatRooms(@AuthenticationPrincipal AuthPrincipal principal,
                                                        @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                        @RequestParam(required = false) String searchType, // 제목, 제목+내용
                                                        @RequestParam(required = false) String q, // 검색키워드
                                                        @RequestParam(required = false) Set<Category> categories){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        log.debug("GET /chat-rooms/my?page={}", pageable.getPageNumber());
        log.debug("GET /chat-rooms/my?page={}&searchType={}&q={}&categories={}", pageable.getPageNumber(), searchType, q, categories);

        Long memberId = principal.getUser().getId();
        Page<ChatRoomListResponseDto> chatRoomPage = chatRoomService.findMyChatRooms(memberId, pageable, searchType, q, categories);
        log.debug("chatRoomPage = {}", chatRoomPage);

        return chatRoomPage;
    }

    // 상세페이지
    @GetMapping("/detail")
    public String detail(){
        log.info("GET /chatroom/detail");
        return "chatroom/detail";
    }

    // 등록페이지
    @GetMapping("/regist")
    public void regist(){
        log.info("GET /chatroom/regist");
    }

    // 수정페이지
    @GetMapping("/update")
    public String update(){
        log.info("GET /chatroom/update");
        return "chatroom/update";
    }
}
