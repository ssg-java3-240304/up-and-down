package com.up.and.down.chatroom.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMember {
    private Long members; // 채팅방에 속한 사람
    private Long creatorId; // 채팅방을 만든 사람
}
