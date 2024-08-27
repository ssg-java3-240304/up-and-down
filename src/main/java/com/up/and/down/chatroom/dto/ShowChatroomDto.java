package com.up.and.down.chatroom.dto;


import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowChatroomDto {
    private Long id;
    private String name; // 채팅방 이름
    private Set<String> categories; // 채팅방 카테고리
    private String nickName; // 채팅방 만든 사람의 닉네임
    private int memberCount; // 참여 인원 수

    public ShowChatroomDto toDto(Chatroom chatroom) {
        return ShowChatroomDto.builder()
                .id(chatroom.getChatroomId())
                .name(chatroom.getName())
                .categories(
                        chatroom.getCategory().stream()
                                .map(Category::getDisplayKorName)
                                .collect(Collectors.toSet())
                )
                .memberCount(chatroom.getMemberIdList().size())
                .build();
    }
}