package com.up.and.down.chatroom.controller;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.service.ChatService;
import com.up.and.down.user.member.entity.Member;
import com.up.and.down.user.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatStompController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    /**
     * 실제로 메시지가 처리하는 곳(새로운 메시지를 실시간으로 주고받는 컨트롤러)
     * 누군가 메시지를 보내면 이 메시지를 받아서 다른 사람에게 보내는 역할
     * @param chatRoomId
     * @param chatDto
     * @return
     */
    @MessageMapping("/chat-rooms/chat/{chatRoomId}") // 메시지가 어디로 가야하는지 정하는 것 ex) chatroomId로 보내며
    public ChatDto chat(@DestinationVariable(value = "chatRoomId") Long chatRoomId,
                        Authentication authentication,
                        ChatDto chatDto){
        log.debug("chatroomId = {}", chatRoomId);
        log.debug("chatDto = {}", chatDto);
        log.debug("authentication = {}", authentication);

        // 현재 인증된 사용자 정보 가져오기
        Long memberId = ((AuthPrincipal) authentication.getPrincipal()).getUser().getId();

        // db에서 member조회하기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        chatDto.setNickname(member.getNickname()); // 닉네임 설정
        chatDto.setNow(LocalDateTime.now());  // 현재 시간을 메시지에 설정

        chatService.saveMessage(chatDto); // 메시지 db에 저장
        return chatDto;
    }

    @MessageMapping("/chat-rooms/enter/{chatRoomId}")
    public void enterChatRoom(@DestinationVariable Long chatRoomId, ChatDto chatDto) {
        List<ChatDto> lastMessages = chatService.findLastChatByChatRoomId(chatRoomId, 0, 20); // 최신 메시지 50개 가져오기
        messagingTemplate.convertAndSendToUser(chatDto.getNickname(), "/sub/chat-rooms/" + chatRoomId, lastMessages);
    }
}
