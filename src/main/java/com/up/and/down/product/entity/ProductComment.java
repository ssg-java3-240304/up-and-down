package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_product_comment")
@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ProductComment extends Comment {
    private Long productId;
    private int stars; // 별점

    public ProductComment(Long id, Long writerId, List<CommentHistory> commentHistoryList, CommentState state, Long productId, int stars) {
        super(id, writerId, commentHistoryList, state);
        this.productId = productId;
        this.stars = stars;
    }
}