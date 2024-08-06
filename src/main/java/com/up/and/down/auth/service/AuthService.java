package com.up.and.down.auth.service;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.user.User;
import com.up.and.down.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new AuthPrincipal(user); // @Data가 @AllArgsConstructor를 같이 선언해주기 때문에 생성자 사용이 가능
    }
}
