package com.up.and.down.search.service;

import com.up.and.down.product.entity.Destination;
import com.up.and.down.search.entity.ProductGroupDoc;
import com.up.and.down.search.repository.ProductGroupDocRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
class SearchServiceTest {
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
    @ValueSource(strings = {"제주도", "거제"})
    @DisplayName("여행지 조회")
    void testFindByDestination(String destinationStr) {
        // given
        Destination destination = Destination.fromKrName(destinationStr);

        // when
        List<ProductGroupDoc> productGroupDocList = this.repo.findByDestination(destination);

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

    private void docToString(List<ProductGroupDoc> docList) {
        docList.forEach(doc -> {
            System.out.println("ProductGroupDoc {");
            System.out.println("\tid=" + doc.getId() + ",");
            System.out.println("\tdestination='" + doc.getDestination() + "',");
            System.out.println("\tnights=" + doc.getNights() + ",");
            System.out.println("\tproductList={");

            doc.getProductList().forEach((key, value) -> {
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
            System.out.println("\t}" + ",");
            System.out.println("\tviewCount=" + doc.getViewCount());
            System.out.println("}");
        });
    }
}