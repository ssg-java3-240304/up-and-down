package com.up.and.down.user.member.service;

import com.up.and.down.user.UserRepository;
import com.up.and.down.user.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void save(Member member) {
        this.userRepository.save(member);
    }
}
