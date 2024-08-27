package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatroomResponseDto {
    private Long chatroomId;
    private String name;
    private Set<Category> category;
    private String description;
    private String nickname;
    private int memberCount;
    private boolean isCreator;
    private boolean isMember;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChatroomResponseDto fromChatroom(Chatroom chatroom, String nickname, Long memberId) {
        return ChatroomResponseDto.builder()
                .chatroomId(chatroom.getChatroomId())
                .name(chatroom.getName())
                .category(chatroom.getCategory())
                .description(chatroom.getDescription())
                .nickname(nickname)
                .memberCount(chatroom.getMemberIdList().size())
                .isCreator(chatroom.getCreatorId().equals(memberId))
                .isMember(chatroom.getMemberIdList().contains(memberId))
                .createdAt(chatroom.getCreatedAt())
                .updatedAt(chatroom.getUpdatedAt())
                .build();
    }
}

