package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomUpdateRequestDto {
    private Long chatRoomId;
    private String name;
    private Set<Category> category;
    private String description;
    private LocalDateTime updatedAt;
}
