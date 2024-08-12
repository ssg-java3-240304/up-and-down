package com.up.and.down.user.member.controller;

import com.up.and.down.auth.service.AuthService;
import com.up.and.down.user.member.dto.KakaoProfileDto;
import com.up.and.down.user.member.service.KakaoSNSService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class KakaoSNSController {
    private final KakaoSNSService kakaoSNSService;
    private final AuthService authService;

    // 카카오 api 호출
    @GetMapping("/login/kakao")
    public String snsApi() {
        String apiURL = kakaoSNSService.reqSnsApi();
        return "redirect:" + apiURL;
    }

    @GetMapping("/member/kakao_login")
    public String kakaoLogin(@RequestParam String code,
                             HttpServletRequest request) {
        log.info("kakaoToken 요청");
        log.info("code = {}", code);
        // URL에 포함된 code를 이용하여 액세스 토큰 발급
        String accessToken = kakaoSNSService.getKakaoAccessToken(code);
        log.info("accessToken = {}", accessToken);

        // 액세스 토큰을 이용하여 카카오 서버에서 유저 정보(닉네임, 이메일) 받아오기
        KakaoProfileDto userInfo = kakaoSNSService.getUserInfo(accessToken);
        log.info("login = {}", userInfo);

        // 네이버 로그인 회원 정보 DB 저장
        kakaoSNSService.saveKakaoMember(userInfo);

        // 로그인
        authService.snsLogin(userInfo.getId(), request);
        return "redirect:/";
    }
}
