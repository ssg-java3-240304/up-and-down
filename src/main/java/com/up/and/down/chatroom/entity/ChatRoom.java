package com.up.and.down.chatroom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tbl_chat_room")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;
    private String name; // 채팅방이름

    @ElementCollection(
            fetch = FetchType.LAZY,
            targetClass = Category.class
    )
    @CollectionTable(
            name = "tbl_chat_room_category",
            joinColumns = @JoinColumn(name = "chat_room_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Category> category; // 카테고리

    private String content; // 채팅방 소개 내용

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "tbl_chat_room_members",
            joinColumns = @JoinColumn(name = "chat_room_id")
    )
    private Set<Long> members; // 채팅방에 속한 사람

    // aggregate
//    @CreatedDate
    private LocalDateTime createAt; // 등록일시
//    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정일시

    @Transient // 컬럼이 생기지 않는 어노테이션
    private LocalDateTime lastChatTime;
}