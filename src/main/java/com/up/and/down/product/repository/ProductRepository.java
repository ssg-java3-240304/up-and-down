package com.up.and.down.product.repository;

import com.up.and.down.product.entity.Product;
import com.up.and.down.user.admin.dto.ProductDestinationInfo;
import com.up.and.down.user.admin.dto.ProductTravelAgencyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
        select new com.up.and.down.user.admin.dto.ProductDestinationInfo(
            p.productInformation.destination,
            count(*))
        from
            Product p
        group by
            p.productInformation.destination
        order by
            count(*)
    """)
    List<ProductDestinationInfo> getDestinationNProdQuantity();

    @Query(value = """
        select
           SUBSTRING_INDEX(SUBSTRING_INDEX(pgp.travel_agency, '/', -1), '.', 1) as travelAgency,
           count(*) as count
        from
           tbl_product_group pg
        join
           tbl_product_group_products pgp
               on pg.id = pgp.product_group_id
        group by
           SUBSTRING_INDEX(SUBSTRING_INDEX(pgp.travel_agency, '/', -1), '.', 1)
        order by
           count
    """, nativeQuery = true)
    List<ProductTravelAgencyInfo> getTravelAgencyNProdQuantity();
}
