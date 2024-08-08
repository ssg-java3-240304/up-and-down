package com.up.and.down.user.member.repository;

import com.up.and.down.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
