package com.up.and.down.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_product_comment")
@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductComment extends Comment {
    private int stars; // 별점

    // 댓글 답글
    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<ProductCommentReply> replyList; // 답글 목록
}
