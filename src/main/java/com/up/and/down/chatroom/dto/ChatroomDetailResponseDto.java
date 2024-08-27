package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomDetailResponseDto {
    private Long chatroomId;
    private String name;
    private Set<Category> category;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChatroomDetailResponseDto fromChatroom(Chatroom chatroom) {
        return new ChatroomDetailResponseDto(
                chatroom.getChatroomId(),
                chatroom.getName(),
                chatroom.getCategory(),
                chatroom.getDescription(),
                chatroom.getCreatedAt(),
                chatroom.getUpdatedAt()
        );
    }
}
