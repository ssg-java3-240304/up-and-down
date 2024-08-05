package com.up.and.down.chatroom.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity(name = "ChatRoom")
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
            fetch = FetchType.EAGER,
            targetClass = Category.class
    )
    @CollectionTable(
            name = "tbl_chat_room_category",
            joinColumns = @JoinColumn(name = "chat_room_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Category> category; // 카테고리

    private String content; // 채팅방 소개 내용

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(
//            name = "tbl_chat_room_members",
//            joinColumns = @JoinColumn(name = "chat_room_id")
//    )
//    private Set<Long> members; // 채팅방에 속한 사람
//    private Long creatorId; // 채팅방을 만든 사람
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_chat_room_members",
            joinColumns = @JoinColumn(name = "chat_room_id")
    )
    private Set<ChatRoomMember> chatRoomMembers;

    @CreatedDate
    private LocalDateTime createAt; // 등록일시
    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정일시
}