package com.up.and.down.user.member.repository;

import com.up.and.down.user.member.entity.SNSMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SNSMemberRepository extends JpaRepository<SNSMember, Long> {
    SNSMember findByUserId(String userId);
}
