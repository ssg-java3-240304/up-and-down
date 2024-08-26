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

    // 메시지 db에 저장
    public void save(ChatDto chatDto) {
        Chat chat = chatDto.toEntity();
        this.chatRepo.save(chat); // 저장후 반환된 엔티티 사용하기
    }

    public List<ChatDto> findByChatroomId(Long chatroomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Chat> chatList = this.chatRepo.findByChatroomId(chatroomId, pageable);
        return chatList.stream().map(Chat::toDto).toList();
    }
}
