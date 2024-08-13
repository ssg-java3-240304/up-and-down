package com.up.and.down.search.repository;

import com.up.and.down.product.entity.Destination;
import com.up.and.down.search.entity.ProductGroupDoc;
import lombok.NonNull;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductGroupDocRepository extends ElasticsearchRepository<ProductGroupDoc, Long> {
    @Override
    @NonNull
    List<ProductGroupDoc> findAll();

    List<ProductGroupDoc> findByDestination(Destination destination);

    List<ProductGroupDoc> findByNights(int nights);
}
