package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    // 댓글 정보
    @Id
    private Long id;
    private String writer; // 작성자
    private String content; // 댓글 내용
    private LocalDateTime createTime; // 생성시간

    // 상태 정보
    private CommentState state; // 댓글 상태
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private List<CommentHistory> historyList; // 댓글 변경사항
}
