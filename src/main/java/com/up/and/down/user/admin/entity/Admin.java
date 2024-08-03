package com.up.and.down.user.admin.entity;

import com.up.and.down.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_admin")
@DiscriminatorValue("admin")
@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin extends User {
    private String empNum; // 사번
    private Department department; // 소속 부서
    private Hierarchy hierarchy; // 직급
    private String officeNumber; // 사내 유선전화번호
    private EmpStatus empStatus; // 재직여부
}
