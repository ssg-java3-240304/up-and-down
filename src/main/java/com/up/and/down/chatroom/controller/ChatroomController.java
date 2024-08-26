package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatRoomListResponseDto;
import com.up.and.down.chatroom.dto.ChatRoomRegistRequestDto;
import com.up.and.down.chatroom.dto.ChatRoomResponseDto;
import com.up.and.down.chatroom.dto.ShowChatRoomDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.service.ChatroomService;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.common.ImageImport;
import com.up.and.down.common.paging.PageCriteria;
import com.up.and.down.user.User;
import com.up.and.down.user.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;


@Slf4j
@Controller
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatroomController {
    private final ChatroomService chatRoomService;

    //이미지 호출하는 클래스 객체 생성


    @GetMapping("/loginCheck")
    @ResponseBody
    public String checkLgoin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "NoPermission";
        } else return "Permission";
    }

    //메인 페이지 조회 -> 동기처리
    @GetMapping("/list")
    public String findAllChatRoomList(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Model model
    ){
        ImageImport imageImport = new ImageImport();

        //이미지 가져오는 코드
        String directoryName = "product";
        String imageName = "reading-glasses.png";
        String imageUrl = imageImport.getImageUrl(directoryName, imageName);

        // 전체 채팅방 목록 가져오기
        Page<ChatroomListResponseDto> chatRoomPage = chatRoomService.findAll(pageable, nickname);
        Page<ShowChatRoomDto> chatRooms = chatRoomService.findAllChatRooms(pageable);

        //페이징 정보 생성
        String url = "chat-rooms/list";
        PageCriteria pageCriteria = new PageCriteria(pageable.getPageNumber(), pageable.getPageSize(), (int) chatRooms.getTotalElements(), url);

        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("pageCriteria", pageCriteria);

        return "chatroom/list";
    }

    // 전체 채팅방 목록
    @GetMapping
    @ResponseBody
    public Page<ChatroomListResponseDto> getAllChatRooms(@PageableDefault(page = 0, size = 10) Pageable pageable,
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

        Page<ChatroomListResponseDto> chatRoomPage = chatRoomService.findAllChatRooms(pageable, searchType, keywords, categories, nickname);
        log.debug("chatRoomPage = {}", chatRoomPage);

}
        return chatRoomPage;
    }
    // 우리모임 채팅방 목록
    @GetMapping("/our")
    @ResponseBody
    public Page<ChatroomListResponseDto> getOurChatRooms(Authentication authentication,
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
        Page<ChatroomListResponseDto> chatRoomPage = chatRoomService.findOurChatRooms(memberId, pageable, searchType, keywords, categories);
        log.debug("chatRoomPage = {}", chatRoomPage);

        return chatRoomPage;
    }
    // 내모임 채팅방 목록
    @GetMapping("/my")
    @ResponseBody
    public Page<ChatroomListResponseDto> getMyChatRooms(Authentication authentication,
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
        Page<ChatroomListResponseDto> chatRoomPage = chatRoomService.findMyChatRooms(memberId, pageable, searchType, keywords, categories);
        log.debug("chatRoomPage = {}", chatRoomPage);

        return chatRoomPage;
    }

    // 상세페이지
    @GetMapping("/{chatRoomId}")
    public String detail(@PathVariable("chatRoomId") Long chatRoomId,
                         @AuthenticationPrincipal AuthPrincipal principal,
                         Model model){

        Long memberId = principal != null ? principal.getUser().getId() : null;

        // 채팅방 상세 정보와 멤버수 가져오기
        ChatroomInfoDto chatRoomInfo = chatRoomService.getChatRoomInfoById(chatRoomId);
        ChatroomResponseDto chatRoom = chatRoomService.findByChatRoom(chatRoomId, memberId);
        int memberCount = chatRoomService.getChatRoomMemberCount(chatRoomId); // 멤버수 조회

        log.info("GET /chatroom/detail");
        log.debug("chatRoom = {}", chatRoom);
        log.debug("chatRoomInfo = {}", chatRoomInfo);
        log.debug("memberCount = {}", memberCount);

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("chatRoomInfo", chatRoomInfo);
        model.addAttribute("memberCount", memberCount);
        return "chatroom/detail";
    }

    // 등록페이지
    @GetMapping("/regist")
    public String regist(){
        log.info("GET /chatroom/regist");
        return "chatroom/regist";
    }
    @PostMapping("/regist")
    public String regist(@ModelAttribute ChatroomRegistRequestDto dto,
                         @AuthenticationPrincipal AuthPrincipal principal,
                         RedirectAttributes redirectAttributes){
        log.debug("dto = {}", dto);
        // 현재 로그인한 사용자 id
        Long creatorId = principal.getUser().getId();
        // 채팅방 등록
        chatRoomService.registChatRoom(dto, creatorId);
        redirectAttributes.addFlashAttribute("message", "채팅방이 등록되었습니다");
        return "redirect:/chatroom/list"; // 등록후에 메인페이지로 리다이렉트
    }


    // 수정페이지
    @GetMapping("/update/{chatRoomId}")
    public String update(@PathVariable("chatRoomId") Long chatRoomId, Model model){
        log.info("GET /chatroom/update");
        ChatroomDetailResponseDto dto = chatRoomService.findById(chatRoomId); // ChatRoomDetailResponseDto 조회용도
        log.debug("dto = {}", dto);
        model.addAttribute("chatRoom", dto);
        return "chatroom/update";
    }
    @PostMapping("/update/{chatRoomId}")
    public String update(@ModelAttribute ChatroomUpdateRequestDto dto,
                         RedirectAttributes redirectAttributes){
        log.debug("dto = {}", dto);
        chatRoomService.update(dto);
        redirectAttributes.addFlashAttribute("message", "채팅방 소개글을 수정했습니다.");
        return "redirect:/chatroom/" + dto.getChatroomId();
    }
}
