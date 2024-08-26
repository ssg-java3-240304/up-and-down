package com.up.and.down.chatroom.entity;

import com.up.and.down.chatroom.dto.ChatDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_chat")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatroomId; // 채팅방 id(fk)
    private Long memberId; // 채팅 멤버 id(fk)
    private String nickname; // 닉네임
    private String message; // 메시지 내용
    private LocalDateTime createdAt; // 생성시간

    public ChatDto toDto() {
        return ChatDto.builder()
                .chatroomId(this.chatroomId)
                .memberId(this.memberId)
                .nickname(this.nickname)
                .message(this.message)
                .createdAt(this.createdAt)
                .build();
    }
}
