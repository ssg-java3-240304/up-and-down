package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private Long chatRoomId;
    private String name; // 채팅방 이름
    private Category category; // 카테고리
    private String content; // 소개글
    private Long creatorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String nickname;
}
