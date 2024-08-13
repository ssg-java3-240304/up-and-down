package com.up.and.down.search.repository;

import com.up.and.down.search.entity.ProductGroupDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductGroupDocRepository extends ElasticsearchRepository<ProductGroupDoc, String> {
}
