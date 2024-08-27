package com.up.and.down.user.admin.repository;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.user.admin.dto.ProductDestinationInfo;
import com.up.and.down.user.admin.dto.ProductTravelAgencyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductStatRepository extends JpaRepository<ProductGroup, Long> {
    @Query("""
        select new com.up.and.down.user.admin.dto.ProductDestinationInfo(
            p.destination,
            sum(p.viewCount))
        from
            ProductGroup p
        group by
            p.destination
        order by
            sum(p.viewCount)
    """)
    List<ProductDestinationInfo> getDestinationNViewCount();

    @Query("""
        select new com.up.and.down.user.admin.dto.ProductDestinationInfo(
            p.destination,
            sum(p.likeCount))
        from
            ProductGroup p
        group by
            p.destination
        order by
            sum(p.likeCount)
    """)
    List<ProductDestinationInfo> getDestinationNLikeCount();

    @Query(value = """
        select
           SUBSTRING_INDEX(SUBSTRING_INDEX(pgp.travel_agency, '/', -1), '.', 1) as travelAgency,
           sum(pg.view_count) as count
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
    List<ProductTravelAgencyInfo> getTravelAgencyNViewCount();

//    @Query(value = """
//        select
//           SUBSTRING_INDEX(SUBSTRING_INDEX(pgp.travel_agency, '/', -1), '.', 1) as travelAgency,
//           sum(pg.like_count) as count
//        from
//           tbl_product_group pg
//        join
//           tbl_product_group_products pgp
//               on pg.id = pgp.product_group_id
//        group by
//           SUBSTRING_INDEX(SUBSTRING_INDEX(pgp.travel_agency, '/', -1), '.', 1)
//        order by
//           count
//    """, nativeQuery = true)
//    List<ProductTravelAgencyInfo> getTravelAgencyNLikeNum();
}
