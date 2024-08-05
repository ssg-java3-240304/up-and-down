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
    private String message; // 메시지 내용
    private LocalDateTime now; // 시간

    public Chat toChat(){
        return Chat.builder()
                .chatRoomId(this.chatRoomId)
//                .memberId(this.)
                .message(this.message)
                .createdAt(this.now)
                .build();
    }
}
