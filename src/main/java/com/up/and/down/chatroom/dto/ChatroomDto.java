package com.up.and.down.chatroom.dto;

import com.up.and.down.chatroom.entity.Category;
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
public class ChatroomDto {
    private Long chatroomId;
    private String name; // 채팅방 이름
    private Set<Category> categories; // 카테고리
    private String description; // 소개글
    private Long creatorId;
    private String creatorNickname;
    private Set<Long> memberIdList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isCreator(Long memberId) {
        return creatorId.equals(memberId);
    }

    public boolean isMember(Long memberId) {
        return memberIdList.contains(memberId);
    }

    public int getMemberCount() {
        return memberIdList.size();
    }
}
