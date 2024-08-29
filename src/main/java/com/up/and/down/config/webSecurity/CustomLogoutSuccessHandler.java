package com.up.and.down.config.webSecurity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.Set;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            if (roles.contains("ADMIN")) {
                response.sendRedirect("/admin/login"); // 관리자는 로그아웃 후 /admin/login 으로 리다이렉트
            } else {
                response.sendRedirect("/"); // 회원은 로그아웃 후 / 로 리다이렉트
            }
        } else {
            response.sendRedirect("/"); // 인증 정보가 없으면 기본적으로 /로 리다이렉트
        }
    }
}
