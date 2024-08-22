package com.up.and.down.user.admin.repository;

import com.up.and.down.user.admin.entity.Admin;
import com.up.and.down.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmpNum(String username);
}
