package com.up.and.down.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Long id;
    private Long commentId; // 댓글 id
    private String content; // 댓글 내용
    private CommentState newState; // 새로운 상태
    private LocalDateTime changeTime; // 변경시간
}
