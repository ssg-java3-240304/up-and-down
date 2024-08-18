package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatRoomListResponseDto;
import com.up.and.down.chatroom.dto.ChatRoomRegistRequestDto;
import com.up.and.down.chatroom.dto.ChatRoomResponseDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.service.ChatRoomService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;


@Slf4j
@Controller
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // [커뮤니티] 메인페이지 보여주기
    @GetMapping("/list")
    public String list(@PageableDefault(page = 0, size = 10) Pageable pageable,
                       Authentication authentication,
                       Model model){

        // 사용자 닉네임 가져오기
        String nickname = "Guest";
        if (authentication != null && authentication.getPrincipal() instanceof AuthPrincipal principal) {
            if (principal.getUser() instanceof Member) {
                nickname = ((Member) principal.getUser()).getNickname();
            }
        }

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
                                                         @RequestParam(required = false) String keywords, // 검색키워드
                                                         @RequestParam(required = false) Set<Category> categories,
                                                         Authentication authentication){ // 로그인된 사용자 정보
        log.debug("GET /chat-rooms/all?page={}", pageable.getPageNumber());
        log.debug("GET /chat-rooms/all?page={}&searchType={}&q={}&categories={}", pageable.getPageNumber(), searchType, keywords, categories);

        // 로그인된 사용자 닉네임 가져오기
        String nickname = "Guest";
        if (authentication != null && authentication.getPrincipal() instanceof AuthPrincipal principal) {
            if (principal.getUser() instanceof Member) {
                nickname = ((Member) principal.getUser()).getNickname();
            }
        }
        log.debug("nickname = {}", authentication);

        Page<ChatRoomListResponseDto> chatRoomPage = chatRoomService.findAllChatRooms(pageable, searchType, keywords, categories, nickname);
        log.debug("chatRoomPage = {}", chatRoomPage);

        return chatRoomPage;
    }
    // 우리모임 채팅방 목록
    @GetMapping("/our")
    @ResponseBody
    public Page<ChatRoomListResponseDto> getOurChatRooms(Authentication authentication,
                                                         @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                         @RequestParam(required = false) String searchType, // 제목, 제목+내용
                                                         @RequestParam(required = false) String keywords, // 검색키워드
                                                         @RequestParam(required = false) Set<Category> categories){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        log.debug("GET /chat-rooms/our?page={}", pageable.getPageNumber());
        log.debug("GET /chat-rooms/our?page={}&searchType={}&q={}&categories={}", pageable.getPageNumber(), searchType, keywords, categories);

        Long memberId = null;
        if (authentication != null && authentication.getPrincipal() instanceof AuthPrincipal principal) {
            if (principal.getUser() instanceof Member) {
                memberId = principal.getUser().getId();
                log.debug("Authenticated Member ID: {}", memberId);
            }
        }
        Page<ChatRoomListResponseDto> chatRoomPage = chatRoomService.findOurChatRooms(memberId, pageable, searchType, keywords, categories);
        log.debug("chatRoomPage = {}", chatRoomPage);

        return chatRoomPage;
    }
    // 내모임 채팅방 목록
    @GetMapping("/my")
    @ResponseBody
    public Page<ChatRoomListResponseDto> getMyChatRooms(Authentication authentication,
                                                        @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                        @RequestParam(required = false) String searchType, // 제목, 제목+내용
                                                        @RequestParam(required = false) String keywords, // 검색키워드
                                                        @RequestParam(required = false) Set<Category> categories){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        log.debug("GET /chat-rooms/my?page={}", pageable.getPageNumber());
        log.debug("GET /chat-rooms/my?page={}&searchType={}&q={}&categories={}", pageable.getPageNumber(), searchType, keywords, categories);

        Long memberId = null;
        if (authentication != null && authentication.getPrincipal() instanceof AuthPrincipal principal) {
            if (principal.getUser() instanceof Member) {
                memberId = principal.getUser().getId();
            }
        }
        Page<ChatRoomListResponseDto> chatRoomPage = chatRoomService.findMyChatRooms(memberId, pageable, searchType, keywords, categories);
        log.debug("chatRoomPage = {}", chatRoomPage);

        return chatRoomPage;
    }

    // 상세페이지
    @GetMapping("/{chatRoomId}")
    public String detail(@PathVariable("chatRoomId") Long chatRoomId,
                         @AuthenticationPrincipal AuthPrincipal principal,
                         Model model){

        Long memberId = principal != null ? principal.getUser().getId() : null;
        ChatRoomResponseDto chatRoom = chatRoomService.findByChatRoom(chatRoomId, memberId);
        log.info("GET /chatroom/detail");
        log.debug("chatRoom = {}", chatRoom);
        model.addAttribute("chatRoom", chatRoom);
        return "chatroom/detail";
    }

    // 등록페이지
    public String regist(){
        log.info("GET /chatroom/regist");
        return "chatroom/regist";
    }
    @PostMapping("/regist")
    public String regist(@ModelAttribute ChatRoomRegistRequestDto dto,
                         @AuthenticationPrincipal AuthPrincipal principal,
                         RedirectAttributes redirectAttributes){
        log.debug("dto = {}", dto);
        // 현재 로그인한 사용자 id
        Long creatorId = principal.getUser().getId();
        // 채팅방 등록
        chatRoomService.registChatRoom(dto, creatorId);
        redirectAttributes.addFlashAttribute("message", "채팅방이 등록되었습니다");
        return "redirect:/chat-rooms/list"; // 등록후에 메인페이지로 리다이렉트
    }


    // 수정페이지
    @GetMapping("/update")
    public String update(){
        log.info("GET /chatroom/update");
        return "chatroom/update";
    }
}
