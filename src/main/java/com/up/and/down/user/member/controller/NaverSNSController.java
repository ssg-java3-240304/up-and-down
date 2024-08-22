package com.up.and.down.user.member.controller;

import com.up.and.down.auth.service.AuthService;
import com.up.and.down.user.member.dto.NaverProfileDto;
import com.up.and.down.user.member.service.NaverSNSService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NaverSNSController {
    private final NaverSNSService naverSNSService;
    private final AuthService authService;

    @GetMapping("/sns_api")
    public String snsApi() throws UnsupportedEncodingException {
        // naver api 호출
        String apiURL = naverSNSService.reqSnsApi();
        return "redirect:" + apiURL;
    }

    // Naver Login API
    @GetMapping("/member/naver_login")
    public String loginToken(@RequestParam("code") String code,
                             @RequestParam("state") String state,
                             HttpServletRequest request) {
//        log.info("POST loginToken");
        // naver 접근 토큰 발급 요청
        String accessToken = naverSNSService.exchangeCodeForAccessToken(code, state);

        // naver 사용자 프로필 api 호출
        NaverProfileDto userInfo= naverSNSService.getUserInfo(accessToken);
//        log.info("userInfo = {}", userInfo);

        // 네이버 로그인 회원 정보 DB 저장
        naverSNSService.saveNaverMember(userInfo);

        // 로그인
        authService.snsLogin(userInfo.getId(), request);
        return "redirect:/";
    }
}
