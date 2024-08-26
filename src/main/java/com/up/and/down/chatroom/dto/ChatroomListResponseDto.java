package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import com.up.and.down.chatroom.entity.ChatroomMember;
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
public class ChatroomListResponseDto {
    private Long chatroomId;
    private String name; // 채팅방 이름
    private Set<Category> category; // 카테고리
    private String description; // 내용
    private Long creatorId; // 채팅방장
    private Set<ChatroomMember> chatroomMember;
    private int memberCount; // 참여인원수
    private LocalDateTime createdAt;
    private String nickname;

    public static ChatroomListResponseDto fromChatRoom(Chatroom chatroom, String nickname, int participantCount) {
        return ChatroomListResponseDto.builder()
                .chatroomId(chatroom.getChatroomId())
                .name(chatroom.getName())
                .category(chatroom.getCategory())
                .description(chatroom.getDescription())
                .creatorId(chatroom.getCreatorId())
                .memberCount(participantCount)
                .nickname(nickname)
                .createdAt(chatroom.getCreatedAt())
                .build();
    }
}
