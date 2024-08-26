package com.up.and.down.chatroom.entity;

import com.up.and.down.user.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity(name = "ChatRoom")
@Table(name = "tbl_chat_room")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Chatroom {

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

    private String description; // 채팅방 소개 내용

    private Long creatorId; // 채팅방을 만든 사람

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_chat_room_member_id",
            joinColumns = @JoinColumn(name = "chat_room_id")
    )
    private Set<Long> memberIdList = new HashSet<>();

    @CreatedDate
    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정일시

    public int getMemberCount() {
        return memberIdList.size();
    }

}
