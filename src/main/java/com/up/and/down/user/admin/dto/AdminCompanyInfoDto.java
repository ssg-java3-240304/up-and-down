package com.up.and.down.user.admin.dto;

import com.up.and.down.user.admin.entity.Admin;
import com.up.and.down.user.admin.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCompanyInfoDto {
    private String companyName; // 회사명
    private String exponentName; // 대표자명
    private String businessNumber; // 사업자신고번호
    private String mailOrderBusinessReportNumber; // 통신판매업 신고번호
    private String email; // 이메일
    private String serviceNumber; // 고객센터 전화번호

    public static AdminCompanyInfoDto fromAdminCompanyInfo(Company adminCompanyList) {
        return new AdminCompanyInfoDto(
                adminCompanyList.getCompanyName(),
                adminCompanyList.getExponentName(),
                adminCompanyList.getBusinessNumber(),
                adminCompanyList.getMailOrderBusinessReportNumber(),
                adminCompanyList.getEmail(),
                adminCompanyList.getServiceNumber()
        );
    }
}
