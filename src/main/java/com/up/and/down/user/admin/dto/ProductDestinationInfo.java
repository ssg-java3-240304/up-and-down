package com.up.and.down.user.admin.dto;

import com.up.and.down.product.entity.Destination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDestinationInfo {
    private Destination destination;
    private Long count;
}
