package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long writerId; // 작성자

    @ElementCollection(targetClass = CommentHistory.class)
    @CollectionTable(
            name = "tbl_comment_history",
            joinColumns = @JoinColumn(name = "comment_id")
    )
    private List<CommentHistory> commentHistory;

    @Enumerated(EnumType.STRING)
    private CommentState state; // 댓글 상태
}