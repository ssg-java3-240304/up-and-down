package com.up.and.down.config.webSecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
    private final LoginAttemptService loginAttemptService;

    @Autowired
    public WebSecurityConfig(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/scss/**", "/vendor/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /**
         * - permitAll() : 모두 허용
         * - authenticated() : 인증된 사용자만 허용
         * - anonymous() : 인증하지 않은 사용자만 허용
         * - hasRole(), hasAnyRole() : 특정 권한이 있는 사용자만 허용
         */
        http.authorizeHttpRequests((registry) -> {
            registry
                    .requestMatchers( // permitAll
                            "/",
                            "/search/**",
                            "/product/**",
                            "/chatroom/*",
                            "/chat-api-room/all",
                            "/member/**",
                            "/sns_api",
                            "/admin/dashboard",
                            "/admin/stat/**"
                    ).permitAll()
                    .requestMatchers( // anonymous
                            "/auth/login/**",
                            "/join/**",
                            "/admin/login",
                            "/admin/register",
                            "/admin/sign/send",
                            "/admin/check"
                    ).anonymous()
                    .requestMatchers( // authenticated
                            "/chat/stomp/**"
                    ).authenticated()
                    .requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN 권한이 있는 사용자만 허용
                    .anyRequest().authenticated();
        });

        /**
         * 폼로그인 설정
         */
        http.formLogin(configurer -> {
            configurer.loginPage("/auth/login") // GET 로그인폼 요청 url (핸들러 작성 필요)
                    .loginProcessingUrl("/auth/login") // POST 로그인처리 url
                    .usernameParameter("username") // name="username"이 아닌 경우 작성 필요
                    .passwordParameter("password") // name="password"가 아닌 경우 작성 필요
                    .successHandler(customAuthenticationSuccessHandler())
                    .permitAll();
        });

        /**
         * 로그아웃설정 - POST요청만 가능하다.
         */
        http.logout(configurer -> {
            configurer.logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/"); // 로그아웃 후 리다이렉트 url (기본값은 로그인페이지)
        });

        return http.build();
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 로그인 후 리다이렉트 경로 설정
    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(loginAttemptService);
    }
}
