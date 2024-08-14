package com.up.and.down.search.entity;

import com.up.and.down.product.entity.Destination;
import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.entity.ProductInformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

@Document(indexName = "product_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGroupDoc {
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Object)
    private Destination destination;
    @Field(type = FieldType.Integer)
    private int nights;

    @Field(type = FieldType.Object)
    private Map<Long, ProductInformation> productList;
    @Field(type = FieldType.Integer)
    private int viewCount;

    public ProductGroup toEntity() {
        return ProductGroup.builder()
                .id(id)
                .destination(destination)
                .nights(nights)
                .productList(productList)
                .viewCount(viewCount)
                .build();
    }
}
