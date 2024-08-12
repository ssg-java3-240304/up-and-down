package com.up.and.down.config.webSecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/scss/**", "/vendor/**");
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
//                    .requestMatchers("/**").permitAll() // 누구나 허용
                    .requestMatchers("/", "/main", "/product", "/chatroom/chat", "/stomp/**", "/api/public", "member/naver_login", "/sns_api").permitAll() // 누구나 허용
                    .requestMatchers("/login/**", "/join/**").anonymous()
//                    .requestMatchers("/admin/**", "/stomp/**").authenticated() // 인증된 사용자만 허용 - 실제 적용
                    .requestMatchers("/admin/**").authenticated() // 인증된 사용자만 허용
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
