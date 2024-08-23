package com.up.and.down.user.admin.entity;

import com.up.and.down.user.Authority;
import com.up.and.down.user.User;
import com.up.and.down.user.admin.dto.AdminDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_admin")
@DiscriminatorValue("admin")
@Data
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Admin extends User {
    @Column(name = "emp_num", nullable = false, unique = true)
    private String empNum; // 사번
    @Enumerated(EnumType.STRING)
    private Department department; // 소속 부서
    @Enumerated(EnumType.STRING)
    private Hierarchy hierarchy; // 직급
    @Column(name = "office_number", nullable = true)
    private String officeNumber; // 사내 유선전화번호
    @Enumerated(EnumType.STRING)
    private EmpStatus empStatus; // 재직여부

    public Admin(Long id, String name, String userId, String password, String phone, String email, Set<Authority> authorities, String empNum, Department department, Hierarchy hierarchy, String officeNumber, EmpStatus empStatus) {
        super(id, name, userId, password, phone, email, authorities);
        this.empNum = empNum;
        this.department = department;
        this.hierarchy = hierarchy;
        this.officeNumber = officeNumber;
        this.empStatus = empStatus;
    }

    @Override
    public String getUserId() {
        return empNum;
    }

    @Override
    public void setUserId(String userId) {
        throw new UnsupportedOperationException("Admin does not use userId");
    }

    public void changeAdminInfo(AdminDto adminDto) {
        this.empNum = adminDto.getEmpNum();
        super.setUserId(adminDto.getEmpNum());
        super.setPassword(adminDto.getPassword());
        super.setPhone(adminDto.getPhone());
        super.setAuthorities(Set.of(Authority.ADMIN));
    }
}
