package com.up.and.down.user.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrowserCountDto {
    private String browserInfo;
    private long visitCount;
}
