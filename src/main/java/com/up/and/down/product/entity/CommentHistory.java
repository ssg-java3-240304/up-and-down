package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_comment_history")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CommentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long commentId; // 댓글 id
    private String content; // 댓글 내용
    @Enumerated(EnumType.STRING)
    private CommentState newState; // 새로운 상태
    private LocalDateTime changeTime; // 변경시간
}