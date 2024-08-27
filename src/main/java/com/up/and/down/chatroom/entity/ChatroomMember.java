package com.up.and.down.chatroom.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomMember {
    private Long chatRoomMemberId; // 채팅방에 속한 사람
}
