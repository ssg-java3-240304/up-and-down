package com.up.and.down.chatroom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;

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
    private Long chatRoomId; // 채팅방 id(fk)
    private String memberId; // 채팅 멤버 id(fk)
    private String message; // 메시지 내용
    @CreatedDate
    private LocalDateTime createdAt; // 생성시간
}
