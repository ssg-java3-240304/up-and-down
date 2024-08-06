package com.up.and.down.chatroom.service;

import com.up.and.down.chatroom.dto.ChatDto;
import com.up.and.down.chatroom.entity.Chat;
import com.up.and.down.chatroom.repository.ChatRepository;
import com.up.and.down.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    // 메시지 db에 저장
    public Chat saveMessage(ChatDto chatDto) {
        Chat chat = chatDto.toChat();
        return chatRepository.save(chat);
    }

    // 메시지 내역 불러오기
    public List<ChatDto> findChatMessageByChatRoomId(Long chatRoomId){
        List<Chat> chats = chatRepository.findByChatRoomId(chatRoomId);
        return chats.stream()
                .map(chat ->
                        new ChatDto(
                                chat.getId(),
                                chat.getChatRoomId(),
                                chat.getNickname(),
                                chat.getMessage(),
                                chat.getCreatedAt()
                        )).collect(Collectors.toList()); // List<ChatDto>로 수집
    }
}
