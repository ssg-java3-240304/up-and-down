package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.ChatRoom;
import com.up.and.down.chatroom.entity.ChatRoomMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListResponseDto {
    private Long chatRoomId;
    private String name;
    private Set<Category> category;
    private String content;
    private Long creatorId;
    private Set<ChatRoomMember> chatRoomMember;
    private LocalDateTime createdAt;

    public static ChatRoomListResponseDto fromChatRoom(ChatRoom chatRoom) {
        return new ChatRoomListResponseDto(
                chatRoom.getChatRoomId(),
                chatRoom.getName(),
                chatRoom.getCategory(),
                chatRoom.getContent(),
                chatRoom.getCreatorId(),
                chatRoom.getChatRoomMembers().stream()
                                .map(member -> new ChatRoomMember(member.getChatRoomMemberId(), member.getNickname()))
                                .collect(Collectors.toSet()),
                chatRoom.getCreateAt()
        );
    }
}
