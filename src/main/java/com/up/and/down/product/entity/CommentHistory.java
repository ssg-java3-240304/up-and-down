package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CommentHistory {
    private String content; // 댓글 내용
    @Enumerated(EnumType.STRING)
    private CommentState newState; // 새로운 상태
    private LocalDateTime changeTime; // 변경시간
}