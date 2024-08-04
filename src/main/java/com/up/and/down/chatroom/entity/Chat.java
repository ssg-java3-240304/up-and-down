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
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "tbl_chat_chatroom",
            joinColumns = @JoinColumn(name = "chat_id")
    )
    @Column(name = "chat_room_id")
    private Set<Long> chatRoomId; // 채팅방 id(fk)

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "tbl_chat_members",
            joinColumns = @JoinColumn(name = "chat_id")
    )
    @Column(name = "member_id")
    private Set<Long> memberId; // 채팅 멤버 id(fk)

    private String content; // 메시지 내용
    @CreatedDate
    private LocalDateTime createdAt; // 생성시간
}
