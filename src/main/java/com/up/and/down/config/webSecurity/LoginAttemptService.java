package com.up.and.down.config.webSecurity;

import com.up.and.down.user.member.dto.LoginInfoDto;
import com.up.and.down.user.member.entity.LoginInfo;
import com.up.and.down.user.member.repository.LoginInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LoginAttemptService {
    private final LoginInfoRepository loginInfoRepository;

    public LoginAttemptService(LoginInfoRepository loginInfoRepository) {
        this.loginInfoRepository = loginInfoRepository;
    }

    public void recordLoginAttempt(String userId, HttpServletRequest request) {
        String browserInfo = getBrowserInfo(request);
        LocalDateTime loginTime = LocalDateTime.now();

        // dto에 값 저장
        LoginInfoDto loginInfoDto = new LoginInfoDto();
        loginInfoDto.setUserId(userId);
        loginInfoDto.setLoginTime(loginTime);
        loginInfoDto.setBrowserInfo(browserInfo);

        // dto -> entity
        LoginInfo loginInfo = loginInfoDto.toLoginInfo();

        // 로그인 로그 저장
        loginInfoRepository.save(loginInfo);
    }

    private String getBrowserInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
//        log.info("userAgent = {}", userAgent);

        if (userAgent.contains("Edg")) {
            return "Edge";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Safari";
        } else {
            return "Unknown";
        }
    }
}
