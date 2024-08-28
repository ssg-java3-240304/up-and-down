package com.up.and.down.config.webSecurity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final LoginAttemptService loginAttemptService;

    public CustomAuthenticationSuccessHandler(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("authentication = {}", authentication);
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("MEMBER")) {
            String memberId = authentication.getName();

            // 로그인 로그 저장
            loginAttemptService.recordLoginAttempt(memberId, request);

            String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
            log.debug(redirectUrl);
            if (redirectUrl != null) {
                request.getSession().removeAttribute("redirectUrl");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect("/");
            }
        } else {
            response.sendRedirect("/"); // 기본 경로 설정
        }
    }
}
