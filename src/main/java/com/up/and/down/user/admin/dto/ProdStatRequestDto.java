package com.up.and.down.user.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdStatRequestDto {
    private String classificationType;
    private String infoType;
}