package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

    private Long id; // chat id
    private Long chatRoomId; // 채팅방id
    private String nickname; // 닉네임
    private Long memberId; // 사용자 id
    private String message; // 메시지 내용
    private LocalDateTime now; // 시간

    public Chat toChatEntity(){
        return Chat.builder()
                .chatRoomId(this.getChatRoomId())
                .memberId(this.getMemberId())
                .nickname(this.getNickname())
                .message(this.getMessage())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ChatDto toChatDto(Chat chat){
        return new ChatDto(
                chat.getId(),
                chat.getChatRoomId(),
                chat.getNickname(),
                chat.getMemberId(),
                chat.getMessage(),
                chat.getCreatedAt()
        );
    }
}
