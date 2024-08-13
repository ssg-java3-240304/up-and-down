package com.up.and.down.user.member.controller;

import com.up.and.down.auth.service.AuthService;
import com.up.and.down.user.member.dto.GoogleProfileDto;
import com.up.and.down.user.member.dto.KakaoProfileDto;
import com.up.and.down.user.member.service.GoogleSNSService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GoogleSNSController {
    private final GoogleSNSService googleSNSService;
    private final AuthService authService;

    // 카카오 api 호출
    @GetMapping("/login/google")
    public String snsApi() {
        String apiURL = googleSNSService.reqSnsApi();
        return "redirect:" + apiURL;
    }

    @GetMapping("/member/google_login")
    public String googleLogin(@RequestParam String code,
                              HttpServletRequest request) {
        // URL에 포함된 code를 이용하여 액세스 토큰 발급
        String accessToken = googleSNSService.getGoogleAccessToken(code);
//        log.info("accessToken = {}", accessToken);

        // userInfo 요청
        GoogleProfileDto userInfo = googleSNSService.getUserInfo(accessToken);
//        log.info("login = {}", userInfo);

        // 구글 로그인 회원 정보 DB 저장
        googleSNSService.saveGoogleMember(userInfo);

        // 로그인
        authService.snsLogin(userInfo.getId(), request);
        return "redirect:/";
    }

}
