package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatRoomListResponseDto;
import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.service.ChatRoomService;
import com.up.and.down.common.paging.PageCriteria;
import com.up.and.down.user.User;
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

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 메인페이지 전체 목록
    @GetMapping("/list")
    public Map<String, Object> list(@AuthenticationPrincipal AuthPrincipal principal,
                                    @PageableDefault(page = 1, size = 10) Pageable pageable,
                                    @RequestParam(required = false) String filter,
                                    Model model) throws AccessDeniedException {
        log.info("GET /chatroom/list");
        log.info("GET /chatroom/list?page={}", pageable.getPageNumber());
        log.debug("principal = {}", principal);

        User user = principal.getUser();

        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());

        Page<ChatRoomListResponseDto> chatRoomPage = chatRoomService.getChatRoom(filter, user, pageable);
        log.debug("chatRoomPate = {}", chatRoomPage);
        model.addAttribute("chatRooms", chatRoomPage.getContent());

        int page = chatRoomPage.getNumber();
        int limit = chatRoomPage.getSize();
        int totalCount = (int) chatRoomPage.getTotalElements();
        String basedUrl = "/app/chatroom/list";
        String url = "";
        if (filter != null) {
            url += "filter+" + filter;
        }
        if (!url.isEmpty()) {
            basedUrl += "?" + url;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomPage", chatRoomPage.getContent());
        response.put("pageCriteria", new PageCriteria(page, limit, totalCount, basedUrl));

        model.addAttribute("pageCriteria", new PageCriteria(page, limit, totalCount, url));
        model.addAttribute("currentFilter", filter); // 현재 필터값
        return response;
    }
    // 메인페이지 검색
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) Set<Category> category,
                                      @PageableDefault(page = 1, size = 10) Pageable pageable,
                                      Model model){
        log.info("GET /chatroom/search");
        log.debug("keyword = {}, category = {}, page = {}, size = {}", keyword, category, pageable.getPageNumber(), pageable.getPageSize());
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        Page<ChatRoomListResponseDto> searchChatRoom = chatRoomService.searchChatRoom(keyword, category, pageable);
        log.debug("searchResult = {}", searchChatRoom);

        int page = searchChatRoom.getNumber(); // 0-based 페이지번호
        int limit = searchChatRoom.getSize();
        int totalCount = (int) searchChatRoom.getTotalElements(); // 전체 페이지 수
        String baseUrl = "/app/chatroom/search";
        String url = "";
        if (keyword != null) {
            url += "keyword=" + keyword;
        }
        if (category != null && !category.isEmpty()) {
            String categories = category.stream()
                    .map(Category::getDisplayName)
                    .collect(Collectors.joining());
            url += "category=" + categories;
        }
        if (url.length() > 0) {
            baseUrl += "?" + url.substring(0, url.length() - 1);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("searchResult", searchChatRoom.getContent());
        response.put("pageCriteria", new PageCriteria(page, limit, totalCount, baseUrl));

        model.addAttribute("pageCriteria", new PageCriteria(page, limit, totalCount, baseUrl));
        model.addAttribute("searchKeyword", keyword); // 현재 검색 키워드
        model.addAttribute("selectedCategories", category); // 선택된 카테고리
        return response;
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
