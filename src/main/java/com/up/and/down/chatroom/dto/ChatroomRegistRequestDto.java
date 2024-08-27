package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import com.up.and.down.chatroom.entity.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomRegistRequestDto {
    private String name; // 채팅방 이름
    private Set<Category> categories; // 카테고리
    private String description; // 소개글

    // chatRoom 엔티티로 변환
    public Chatroom toChatroom(Long creatorId){
        return Chatroom.builder()
                .name(this.name)
                .category(this.categories) // 카테고리 설정
                .description(this.description)
                .creatorId(creatorId)
                .build();
    }
}
