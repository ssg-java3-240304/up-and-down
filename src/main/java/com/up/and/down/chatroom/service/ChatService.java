package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.repository.ChatRepository;
import com.up.and.down.user.member.entity.Member;
import com.up.and.down.user.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepo;
    private final MemberRepository memberRepo;

    // 사용자 id로 닉네임 조회하기
    public String getNicknameById(Long memberId) {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        return member.getNickname();
    }

    // 메시지 db에 저장
    public void save(ChatDto chatDto) {
        Chat chat = chatDto.toEntity();
        this.chatRepo.save(chat); // 저장후 반환된 엔티티 사용하기
    }

    // chatRoomId로 모든 채팅 메시지 가져오기
    public List<ChatDto> findChatMessageByChatRoomId(Long chatRoomId, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        List<Chat> chatPage = chatRepo.findChatMessageByChatRoomId(chatRoomId, pageable);
        // 로그 추가
//        List<ChatDto> chatDtos = chatPage.stream().map(ChatDto::toChatDto).collect(Collectors.toList());
//        chatDtos.forEach(chatDto -> log.debug("모든 채팅 메시지 ChatID: {}", chatDto.getId()));
//        return chatPage.stream().map(ChatDto::toChatDto).collect(Collectors.toList());
        return null;
    }

    // 최근 메시지 가져오기
    public List<ChatDto> findLastChatByChatRoomId(Long chatRoomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Chat> chatList = chatRepo.findLastChatByChatRoomId(chatRoomId, pageable);
        // 로그 추가
//        List<ChatDto> chatDtos = chatList.stream().map(ChatDto::toChatDto).collect(Collectors.toList());
//        chatDtos.forEach(chatDto -> log.debug("최근 메시지 ChatID: {}", chatDto.getId()));
//        return chatList.stream().map(ChatDto::toChatDto).collect(Collectors.toList());
        return null;
    }
}
