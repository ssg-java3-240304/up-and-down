package com.up.and.down.search.service;

import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
class ElasticsearchTest {
    @Autowired
    private ProductGroupDocRepository repo;

    @Test
    @DisplayName("전체 조회")
    void testFindAll() {
        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findAll();

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
        assertThat(productGroupDocList).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    @DisplayName("숙박일 조회")
    void testFindByNights(int nights) {
        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findByNights(nights);

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
        assertThat(productGroupDocList).isNotEmpty();
    }

    @Test
    @DisplayName("아이디 조회")
    void testFindById() {
        // given
        List<Long> idList = this.repo.findAll().stream()
                .map(ProductGroupDoc::getId)
                .toList();

        // when
        for (Long id : idList) {
            System.out.println(id);
            ProductGroupDoc productGroupDoc = this.repo.findById(id).orElse(null);

            // then
            assertThat(productGroupDoc).isNotNull();
            assertThat(productGroupDoc.getId()).isEqualTo(id);
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
            // id에 해당하는 ProductGroupDoc 의 조회수를 가져옴
            ProductGroupDoc productGroupDoc = this.repo.findById(idList.get(i)).orElseThrow(
                    () -> new RuntimeException("ProductGroupDoc not found with id: " + idList.get(curId))
            );
            int curViewCount = productGroupDoc.getViewCount();

            productGroupDoc.setViewCount(curViewCount + 1);

            this.repo.save(productGroupDoc);
            
            // then
            // 조회수가 증가했는지 확인
            ProductGroupDoc updateProductGroupDoc = this.repo.findById(idList.get(i)).orElseThrow(
                    () -> new RuntimeException("ProductGroupDoc not found with id: " + idList.get(curId))
            );
            assertThat(updateProductGroupDoc.getViewCount()).isEqualTo(curViewCount + 1);
            System.out.println("check success!!!");
        }
    }

    @Test
    @DisplayName("조회수로 조회 상위 4개만")
    void testFindByViewCountTop4() {
        // when
        List<ProductGroupDoc> top4ProductGroupList = this.repo.findTop4ByOrderByViewCountDesc();

        // then
        assertThat(top4ProductGroupList).isNotNull();
        assertThat(top4ProductGroupList).isNotEmpty();
        assertThat(top4ProductGroupList).hasSize(4);
        // 각 항목이 이전 항목보다 조회수가 크거나 같음을 확인
        for (int i = 0; i < top4ProductGroupList.size() - 1; i++) {
            assertThat(top4ProductGroupList.get(i).getViewCount())
                    .isGreaterThanOrEqualTo(top4ProductGroupList.get(i + 1).getViewCount());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"제주도", "휴식", "자연", "로맨틱"})
    @DisplayName("searchKeywords 로 조회")
    void testFindBySearchKeywords(String keyword) {
        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findBySearchKeywords(keyword);

        // then
        docToString(productGroupDocList);
        assertThat(productGroupDocList).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
            "제주도, 2",
            "거제, 1"
    })
    @DisplayName("키워드, 숙박일로 조회")
    void testFindBySearchKeywordsAndNights(String searchKeywords, int nights) {
        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findBySearchKeywordsAndNights(searchKeywords, nights);

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
        assertThat(productGroupDocList).isNotEmpty();
    }

    private void docToString(List<ProductGroupDoc> docList) {
        docList.forEach(doc -> {
            System.out.println("ProductGroupDoc {");
            System.out.println("\tid=" + doc.getId() + ",");
            System.out.println("\tsearchKeywords=" + doc.getSearchKeywords() + ",");
            System.out.println("\tdestination='" + doc.getDestination() + "',");
            System.out.println("\tnights=" + doc.getNights() + ",");
            System.out.println("\tproductList={");
            System.out.println("\t\t" + doc.getProductListJson());
            System.out.println("\t}" + ",");
            System.out.println("\tviewCount=" + doc.getViewCount());
            System.out.println("}");
        });
    }
}