package com.up.and.down.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name = "tbl_admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Long adminId;
    private String empNum;
    private String adminPassword;
    private String adminName;
    private String adminPhone;
    private Department department;
    private Hierarchy hierarchy;
    private String officeNumber;
    private String adminEmail;
    private EmpStatus empStatus;
}
