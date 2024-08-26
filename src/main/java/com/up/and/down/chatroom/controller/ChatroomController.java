package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ShowChatroomDto;
import com.up.and.down.chatroom.service.ChatroomService;
import com.up.and.down.common.ImageImport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        } else return "Permission";
    }

    //메인 페이지 조회 -> 동기처리
    @GetMapping("/list")
    public String findAllChatRoomList(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            Model model
    ){

        //이미지 가져오는 코드
        ImageImport imageImport = new ImageImport();
        String directoryName = "product";
        String imageName = "reading-glasses.png";
        String imageUrl = imageImport.getImageUrl(directoryName, imageName);

        //페이징 정보 생성
        Page<ShowChatroomDto> chatRooms = chatroomService.findAllChatRooms(pageable);

        // Model에 데이터 전달
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("chatRooms", chatRooms);  // 실제 데이터 리스트
        model.addAttribute("currentPage", chatRooms.getNumber());
        model.addAttribute("totalPages", chatRooms.getTotalPages());
        model.addAttribute("totalCount", chatRooms.getTotalElements());

        return "chatroom/list";
    }
}
