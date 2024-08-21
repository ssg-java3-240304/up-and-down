package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetailResponseDto {
    private Long chatRoomId;
    private String name;
    private Set<Category> category;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChatRoomDetailResponseDto fromChatRoom(ChatRoom chatRoom) {
        return new ChatRoomDetailResponseDto(
                chatRoom.getChatRoomId(),
                chatRoom.getName(),
                chatRoom.getCategory(),
                chatRoom.getDescription(),
                chatRoom.getCreatedAt(),
                chatRoom.getUpdatedAt()
        );
    }
}
