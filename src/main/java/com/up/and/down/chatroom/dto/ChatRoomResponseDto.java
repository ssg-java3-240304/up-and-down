package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
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
public class ChatRoomResponseDto {
    private Long chatRoomId;
    private String name;
    private Set<Category> category;
    private String description;
    private String nickname;
    private int memberCount;
    private boolean isCreator;
    private boolean isMember;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChatRoomResponseDto fromChatRoom(ChatRoom chatRoom, String nickname, Long memberId) {
        return ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .name(chatRoom.getName())
                .category(chatRoom.getCategory())
                .description(chatRoom.getDescription())
                .nickname(nickname)
                .memberCount(chatRoom.getMemberIdList().size())
                .isCreator(chatRoom.getCreatorId().equals(memberId))
                .isMember(chatRoom.getMemberIdList().contains(memberId))
//                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }
}

