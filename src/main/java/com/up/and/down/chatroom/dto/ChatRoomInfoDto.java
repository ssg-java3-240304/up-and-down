package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomInfoDto {
    private String name; // 채팅방 이름
    private Set<Category> categories; // 카테고리
    private int memberCount; // 참여 인원수
}
