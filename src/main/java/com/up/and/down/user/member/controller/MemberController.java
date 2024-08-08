package com.up.and.down.user.member.controller;

import com.up.and.down.user.member.dto.MemberAccountInfoDto;
import com.up.and.down.user.member.dto.MemberDto;
import com.up.and.down.user.member.dto.PrivacyInfoDto;
import com.up.and.down.user.member.entity.Gender;
import com.up.and.down.user.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/join/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 step1. 약관동의
    @GetMapping("/terms")
    public String terms() {
        return "join/member/terms";
    }

    // 회원가입 step2. 개인정보
    @GetMapping("/privacyInfo")
    public String privacyInfo() {
        return "join/member/privacyInfo";
    }

    @GetMapping("/memberAccountInfo")
    public String memberAccountInfo() {
        return "join/member/memberAccountInfo";
    }

    @GetMapping("/registMember")
    public String registMember() {
        return "join/member/registMember";
    }

    // 회원가입 step3. 계정정보
    @PostMapping("/privacyInfo")
    public String privacyInfo(@ModelAttribute PrivacyInfoDto privacyInfoDto) {
        log.info("POST privacyInfo");
        log.info("privacyInfoDto = {}", privacyInfoDto);
        memberService.insertPrivacy(privacyInfoDto);
        return "redirect:/join/member/memberAccountInfo";
    }

    @PostMapping("/memberAccountInfo")
    public String memberAccountInfo(@ModelAttribute MemberAccountInfoDto memberAccountInfoDto) {
        log.info("POST memberAccountInfoDto");
        log.info("privacyInfoDto = {}", memberAccountInfoDto);
        String encryptedPassword = passwordEncoder.encode(memberAccountInfoDto.getPassword());
        memberAccountInfoDto.setPassword(encryptedPassword);
        log.info("privacyInfoDto = {}", memberAccountInfoDto);
        memberService.insertMemberAccount(memberAccountInfoDto);
        memberService.register();
        return "redirect:/join/member/registMember";
    }

    // 인증 메세지 전송
    @PostMapping("/sign/send")
    @ResponseBody
    public ResponseEntity<?> sendSMS(@RequestParam("to") String to) throws Exception {
        String resultNum = memberService.sendAuthorizationCode(to);
        String result;
        if(!resultNum.equals("false")) {
            result = "success";
        }else {
            result = "false";
        }
        return ResponseEntity.ok(Map.of(
                "result", result,
                "resultNum", resultNum
        ));
    }


}
