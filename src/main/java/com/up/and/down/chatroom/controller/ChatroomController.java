package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.*;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.service.ChatroomService;
import com.up.and.down.common.ImageImport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatroomController {
    private final ChatroomService chatroomService;

    @GetMapping("/loginCheck")
    @ResponseBody
    public String checkLgoin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "NoPermission";
        } else{
            return "Permission";
        }
    }

    //메인 페이지 조회 -> 동기처리
    @GetMapping("/list")
    public String findAllChatroomList(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Model model
    ){

        //이미지 가져오는 코드
        ImageImport imageImport = new ImageImport();
        String directoryName = "product";
        String imageName = "reading-glasses.png";
        String imageUrl = imageImport.getImageUrl(directoryName, imageName);

        //페이징 정보 생성
        Page<ShowChatroomDto> chatroomList = chatroomService.findAllChatroom(pageable);

        // Model에 데이터 전달
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("themeList", Category.values());
        model.addAttribute("chatroomList", chatroomList);  // 실제 데이터 리스트
        model.addAttribute("currentPage", chatroomList.getNumber());
        model.addAttribute("totalPages", chatroomList.getTotalPages());
        model.addAttribute("totalCount", chatroomList.getTotalElements());

        return "chatroom/list";
    }

    // 등록페이지
    @GetMapping("/regist")
    public String getRegist(Model model){
        log.info("GET /chatroom/regist");

        model.addAttribute("themeList", Category.values());

        return "chatroom/regist";
    }

    @PostMapping("/regist")
    public String postRegist(
            @ModelAttribute ChatroomRegistRequestDto dto,
            @AuthenticationPrincipal AuthPrincipal principal,
            RedirectAttributes redirectAttributes
    ){
        log.debug("dto = {}", dto);
        // 현재 로그인한 사용자 id
        Long creatorId = principal.getUser().getId();
        // 채팅방 등록
        chatroomService.registChatroom(dto, creatorId);
        redirectAttributes.addFlashAttribute("message", "채팅방이 등록되었습니다");
        return "redirect:/chatroom/list"; // 등록후에 메인페이지로 리다이렉트
    }

    // 상세페이지
    @GetMapping("/{chatroomId}")
    public String detail(
            @PathVariable("chatroomId") Long chatroomId,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ){
        log.info("GET /chatroom/detail");

        ChatroomDto chatroomDto = chatroomService.findByChatroomId(chatroomId);
        Long currentUserId = (principal != null) ? principal.getUser().getId() : null;

        model.addAttribute("chatroomDto", chatroomDto);
        model.addAttribute("currentUserId", currentUserId);

        return "chatroom/detail";
    }

    // 수정페이지
    @GetMapping("/update/{chatroomId}")
    public String update(@PathVariable("chatroomId") Long chatroomId, Model model){
        log.info("GET /chatroom/update");
        ChatroomDto dto = chatroomService.findByChatroomId(chatroomId); // ChatroomDetailResponseDto 조회용도
        log.debug("dto = {}", dto);

        model.addAttribute("chatroom", dto);
        model.addAttribute("themeList", Category.values());

        return "chatroom/update";
    }

    @PostMapping("/update/{chatroomId}")
    public String update(@ModelAttribute ChatroomUpdateRequestDto dto,
                         RedirectAttributes redirectAttributes){
        log.debug("dto = {}", dto);
        chatroomService.update(dto);
        redirectAttributes.addFlashAttribute("message", "채팅방 소개글을 수정했습니다.");
        return "redirect:/chatroom/" + dto.getChatroomId();
    }

}
