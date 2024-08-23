package com.up.and.down.user.admin.repository;

import com.up.and.down.product.entity.ProductGroup;
import com.up.and.down.user.admin.dto.ProductDestinationInfo;
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
            p.destination
    """)
    List<ProductDestinationInfo> getDestinationNViewCount();
}
