package com.up.and.down.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_product_comment_reply")
@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class ProductCommentReply extends Comment {
}
