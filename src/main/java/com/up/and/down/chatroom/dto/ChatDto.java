package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {

    private Long chatroomId; // 채팅방id
    private Long memberId; // 사용자 id
    private String nickname; // 닉네임
    private String message; // 메시지 내용
    private LocalDateTime createdAt; // 시간

    public Chat toEntity(){
        return Chat.builder()
                .chatroomId(this.chatroomId)
                .memberId(this.memberId)
                .nickname(this.nickname)
                .message(this.message)
                .createdAt(this.createdAt)
                .build();
    }
}
