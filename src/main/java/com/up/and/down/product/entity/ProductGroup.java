package com.up.and.down.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name = "tbl_product_group")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Destination description;
    private int nights;

    @ElementCollection
    @CollectionTable(
            name = "tbl_product_group_products",
            joinColumns = @JoinColumn(name = "product_group_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "product_information")
    private Map<Long, ProductInformation> productList;

    private int viewCount;
}
