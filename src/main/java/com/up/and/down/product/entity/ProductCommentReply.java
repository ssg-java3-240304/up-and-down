package com.up.and.down.product.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.List;

@Embeddable
@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ProductCommentReply extends Comment {
    private Long parentCommentId;

    public ProductCommentReply(Long id, Long writerId, List<CommentHistory> commentHistoryList, CommentState state, Long parentCommentId) {
        super(id, writerId, commentHistoryList, state);
        this.parentCommentId = parentCommentId;
    }
}