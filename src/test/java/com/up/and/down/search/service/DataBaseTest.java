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

    @Test
    @DisplayName("상품 그룹 조회수 증가")
    void testIncreaseViewCount() {
        // given
        List<Long> idList = List.of(1L, 3L, 5L, 7L, 9L);

        // when
        for (int i = 0; i < idList.size(); i++) {
            int curId = i;
            // id에 해당하는 ProductGroup 의 조회수를 가져옴
            ProductGroup productGroup = this.repo.findById(idList.get(i)).orElseThrow(
                    () -> new RuntimeException("ProductGroup not found with id: " + idList.get(curId))
            );
            int curViewCount = productGroup.getViewCount();

            productGroup.increaseViewCount();

            this.repo.save(productGroup);

            // then
            // 조회수가 증가했는지 확인
            ProductGroup updateProductGroup = this.repo.findById(idList.get(i)).orElseThrow(
                    () -> new RuntimeException("ProductGroup not found with id: " + idList.get(curId))
            );
            assertThat(updateProductGroup.getViewCount()).isEqualTo(curViewCount + 1);
            System.out.println("check success!!!");
        }
    }
}
