package com.up.and.down.user.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    private int id;
    private String companyName; // 회사명
    private String exponentName; // 대표자명
    private String businessNumber; // 사업자신고번호
    private String mailOrderBusinessReportNumber; // 통신판매업 신고번호
    private String email; // 이메일
    private String serviceNumber; // 고객센터 전화번호
}
