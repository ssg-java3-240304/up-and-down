package com.up.and.down.chatroom.controller;

import com.up.and.down.chatroom.dto.ShowChatroomDto;
import com.up.and.down.chatroom.service.ChatroomService;
import com.up.and.down.common.ImageImport;
import com.up.and.down.common.paging.PageCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


@Slf4j
@Controller
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
public class ChatroomController {
    private final ChatroomService chatroomService;

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

        Page<ShowChatroomDto> chatRooms = chatroomService.findAllChatRooms(pageable);

        //페이징 정보 생성
        String url = "chat-rooms/list";
        PageCriteria pageCriteria = new PageCriteria(pageable.getPageNumber(), pageable.getPageSize(), (int) chatRooms.getTotalElements(), url);

        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("pageCriteria", pageCriteria);

        return "chatroom/list";
    }



}


