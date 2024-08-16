package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.entity.ChatRoomMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomListResponseDto {
    private Long chatRoomId;
    private String name; // 채팅방 이름
    private Set<Category> category; // 카테고리
    private String description; // 내용
    private Long creatorId; // 채팅방장
    private Set<ChatRoomMember> chatRoomMember;
    private int memberCount; // 참여인원수
    private LocalDateTime createdAt;
    private String nickname;

    public static ChatRoomListResponseDto fromChatRoom(ChatRoom chatRoom, String nickname, int participantCount) {
        return ChatRoomListResponseDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .name(chatRoom.getName())
                .category(chatRoom.getCategory())
                .description(chatRoom.getDescription())
                .creatorId(chatRoom.getCreatorId())
                .memberCount(participantCount)
                .nickname(nickname)
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
