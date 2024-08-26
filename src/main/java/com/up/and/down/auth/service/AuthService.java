package com.up.and.down.auth.service;

import com.up.and.down.auth.principal.AuthPrincipal;
import com.up.and.down.user.User;
import com.up.and.down.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new AuthPrincipal(user); // @Data가 @AllArgsConstructor를 같이 선언해주기 때문에 생성자 사용이 가능
    }

    // NAVER 로그인
    public void snsLogin(String userId, HttpServletRequest request) {
        User user = userRepository.findByUsername(userId).orElseThrow();
        AuthPrincipal authPrincipal = new AuthPrincipal(user);
//        log.info("authPrincipal = {}", authPrincipal);
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                authPrincipal,
                authPrincipal.getPassword(),
                authPrincipal.getAuthorities()
        );
//        log.info("newAuthentication = {}", newAuthentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(newAuthentication);
//        log.info("securityContext = {}", securityContext);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }
}
