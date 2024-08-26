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
    private Set<String> categories; // 카테고리
    private String description; // 소개글

    // 문자열 카테고리 목록을 Category enum으로 변환
    public Set<Category> toCategorySet() {
        return categories.stream()
                .map(name -> {
                    try {
                        return Category.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("존재하지 않는 카테고리입니다: " + name, e);
                    }
                })
                .collect(Collectors.toSet());
    }

    // chatRoom 엔티티로 변환
    public Chatroom toChatroom(Long creatorId){
        return Chatroom.builder()
                .name(this.name)
                .category(this.toCategorySet()) // 카테고리 설정
                .description(this.description)
                .creatorId(creatorId)
                .build();
    }
}
