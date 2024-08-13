package com.up.and.down.search.service;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.product.repository.ProductGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DataBaseTest {
    @Autowired
    private ProductGroupRepository repo;

    @Test
    @DisplayName("아이디 조회")
    void testFindById() {
        // given
        List<Long> idList = this.repo.findAll().stream()
                .map(ProductGroup::getId)
                .toList();

        // when
        for (Long id : idList) {
            System.out.println(id);
            ProductGroup productGroup = this.repo.findById(id).orElse(null);

            // then
            assertThat(productGroup).isNotNull();
            assertThat(productGroup.getId()).isEqualTo(id);
        }

        System.out.println("check success!!!");
    }
}
