package com.up.and.down.user.admin.dto;

import com.up.and.down.product.entity.Destination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDestinationInfo {
    private String destination;
    private Long count;

    public ProductDestinationInfo(Destination destination, Long count) {
        this.destination = destination.getKorName(); // 한글명을 사용
        this.count = count;
    }
}
