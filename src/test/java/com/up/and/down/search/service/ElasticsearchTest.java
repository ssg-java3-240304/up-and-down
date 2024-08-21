package com.up.and.down.search.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.up.and.down.product.entity.Destination;
import com.up.and.down.product.entity.ProductInformation;
import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
class ElasticsearchTest {
    @Autowired
    private ProductGroupDocRepository repo;
    @Autowired
    private ProductListJsonConvertService jsonConvertService;

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
    @ValueSource(strings = {"제주도", "휴식", "자연", "로맨틱"})
    @DisplayName("키워드로 조회")
    void testFindBySearchKeywords(String searchKeyword) {
        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findBySearchKeywords(searchKeyword);

        // then
        docToString(productGroupDocList);
        assertThat(productGroupDocList).isNotNull();
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
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-09-01", "2024-09-02", "2024-09-03"})
    @DisplayName("여행 출발일로 조회")
    void testFindByStartDate(String startDateStr) {
        // given
        LocalDate startDate = LocalDate.parse(startDateStr);

        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findByStartDate(startDate);

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
        // 각 항목의 startDate가 검색한 날짜와 일치하는지 확인
        for (ProductGroupDoc productGroupDoc : productGroupDocList) {
            assertThat(productGroupDoc.getStartDate()).isEqualTo(startDate);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-09-01", "2024-09-02", "2024-09-03"})
    @DisplayName("여행 출발일 이후로 조회")
    void testFindByStartDateAfter(String startDateStr) {
        // given
        LocalDate startDate = LocalDate.parse(startDateStr);

        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findByStartDateAfter(startDate);

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
        // 각 항목의 startDate가 검색한 날짜 이후인지 확인
        for (ProductGroupDoc productGroupDoc : productGroupDocList) {
            assertThat(productGroupDoc.getStartDate()).isAfterOrEqualTo(startDate);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "제주도, 2",
            "거제, 1"
    })
    @DisplayName("키워드, 숙박일로 조회")
    void testFindBySearchKeywordsAndNights(String searchKeyword, int nights) {
        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findBySearchKeywordsAndNights(searchKeyword, nights);

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
            "제주도, 2024-09-01",
            "휴식, 2024-09-02"
    })
    @DisplayName("키워드와 여행 출발일 이후로 조회")
    void testFindBySearchKeywordsAndStartDateAfter(String searchKeyword, String startDateStr) {
        // given
        LocalDate startDate = LocalDate.parse(startDateStr);

        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findBySearchKeywordsAndStartDateAfter(searchKeyword, startDate);

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
        // 각 항목의 searchKeywords가 검색한 키워드를 포함하고 startDate가 검색한 날짜 이후인지 확인
        for (ProductGroupDoc productGroupDoc : productGroupDocList) {
            assertThat(productGroupDoc.getSearchKeywords()).contains(searchKeyword);
            assertThat(productGroupDoc.getStartDate()).isAfterOrEqualTo(startDate);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2, 2024-09-01",
            "3, 2024-09-02"
    })
    @DisplayName("숙박일과 여행 출발일 이후로 조회")
    void testFindByNightsAndStartDateAfter(int nights, String startDateStr) {
        // given
        LocalDate startDate = LocalDate.parse(startDateStr);

        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findByNightsAndStartDateAfter(nights, startDate);

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
        // 각 항목의 nights가 검색한 숙박일과 같고 startDate가 검색한 날짜 이후인지 확인
        for (ProductGroupDoc productGroupDoc : productGroupDocList) {
            assertThat(productGroupDoc.getNights()).isEqualTo(nights);
            assertThat(productGroupDoc.getStartDate()).isAfterOrEqualTo(startDate);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "제주도, 2, 2024-09-01",
            "거제, 2, 2024-09-02"
    })
    @DisplayName("키워드, 숙박일, 여행 출발일 이후로 조회")
    void testFindBySearchKeywordsAndNightsAndStartDateAfter(String searchKeyword, int nights, String startDateStr) {
        // given
        LocalDate startDate = LocalDate.parse(startDateStr);

        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findBySearchKeywordsAndNightsAndStartDateAfter(searchKeyword, nights, startDate);

        // then
        docToString(productGroupDocList);

        assertThat(productGroupDocList).isNotNull();
        // 각 항목의 searchKeywords가 검색한 키워드를 포함하고 nights가 검색한 숙박일과 같으며 startDate가 검색한 날짜 이후인지 확인
        for (ProductGroupDoc productGroupDoc : productGroupDocList) {
            assertThat(productGroupDoc.getSearchKeywords()).contains(searchKeyword);
            assertThat(productGroupDoc.getNights()).isEqualTo(nights);
            assertThat(productGroupDoc.getStartDate()).isAfterOrEqualTo(startDate);
        }
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
        List<ProductGroupDoc> productGroupDocList = this.repo.findAll();

        // when
        for (int i = 0; i < 10; i++) {
            int curId = i;
            // id에 해당하는 ProductGroupDoc 의 조회수를 가져옴
            ProductGroupDoc productGroupDoc = this.repo.findById(productGroupDocList.get(i).getId()).orElseThrow(
                    () -> new RuntimeException("ProductGroupDoc not found with id: " + productGroupDocList.get(curId))
            );
            int curViewCount = productGroupDoc.getViewCount();

            productGroupDoc.setViewCount(curViewCount + 1);

            this.repo.save(productGroupDoc);
            
            // then
            // 조회수가 증가했는지 확인
            ProductGroupDoc updateProductGroupDoc = this.repo.findById(productGroupDocList.get(i).getId()).orElseThrow(
                    () -> new RuntimeException("ProductGroupDoc not found with id: " + productGroupDocList.get(curId))
            );
            assertThat(updateProductGroupDoc.getViewCount()).isEqualTo(curViewCount + 1);
            System.out.println("check success!!!");
        }
    }

    @Test
    @DisplayName("조회수순으로 조회")
    void testFindByViewCount() {
        // when
        List<ProductGroupDoc> productGroupList = this.repo.findAllByOrderByViewCountDesc();

        // then
        assertThat(productGroupList).isNotNull();
        assertThat(productGroupList).isNotEmpty();
        // 각 항목이 이전 항목보다 조회수가 크거나 같음을 확인
        for (int i = 0; i < productGroupList.size() - 1; i++) {
            assertThat(productGroupList.get(i).getViewCount())
                    .isGreaterThanOrEqualTo(productGroupList.get(i + 1).getViewCount());
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

    public void docToString(List<ProductGroupDoc> docList) {
        docList.forEach(doc -> {
            System.out.println("ProductGroupDoc {");
            System.out.println("\tid=" + doc.getId() + ",");
            System.out.println("\tsearchKeywords=" + doc.getSearchKeywords() + ",");
            System.out.println("\tdestination='" + doc.getDestination() + "',");
            System.out.println("\tnights=" + doc.getNights() + ",");
            System.out.println("\tstartDate=" + doc.getStartDate() + ",");
            System.out.println("\tproductList={");
            Map<Long, ProductInformation> productList;
            try {
                productList = jsonConvertService.convertJsonToProductList(doc.getProductListJson());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                productList = null;
            }
            if (productList != null) {
                productList.forEach((key, value) -> {
                    System.out.println("\t\t" + key + " = ProductInformation {");
                    System.out.println("\t\t\tdestination='" + value.getDestination() + "',");
                    System.out.println("\t\t\tnights=" + value.getNights() + ",");
                    System.out.println("\t\t\ttitle='" + value.getTitle() + "',");
                    System.out.println("\t\t\tstart_date='" + value.getStart_date() + "',");
                    System.out.println("\t\t\tprice=" + value.getPrice() + ",");
                    System.out.println("\t\t\tthumbnailUrl='" + value.getThumbnailUrl() + "',");
                    System.out.println("\t\t\ttravelAgency='" + value.getTravelAgency() + "',");
                    System.out.println("\t\t\tdetailUrl='" + value.getDetailUrl() + "'");
                    System.out.println("\t\t},");
                });
            } else {
                System.out.println("\t\t<empty product list>");
            }
            System.out.println("\t},");

            System.out.println("\tviewCount=" + doc.getViewCount());
            System.out.println("}");
        });
    }
}