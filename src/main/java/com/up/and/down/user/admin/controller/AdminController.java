package com.up.and.down.user.admin.controller;

import com.up.and.down.user.admin.dto.AdminDto;
import com.up.and.down.user.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String adminLogin() {
        return "login/adminLogin";
    }

    @GetMapping("/register")
    public String adminRegist() {
        return "join/admin/adminAccountInfo";
    }

    @PostMapping("/register")
    public String adminRegister(@ModelAttribute AdminDto adminDto,
                                RedirectAttributes redirectAttributes) {
        String encryptedPassword = passwordEncoder.encode(adminDto.getPassword());
        adminDto.setPassword(encryptedPassword);
        boolean result = adminService.adminRegister(adminDto);
        String message;
        if(!result)
            message = "등록되었습니다🎈";
        else
            message = "등록을 실패하였습니다. 다시 등록해주세요🎈";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/login";
    }

    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<?> check(@RequestParam String empNum) {
       String result = adminService.checkEmpNum(empNum);
        return ResponseEntity.ok(Map.of(
                "result", result
        ));
    }

    @PostMapping("/sign/send")
    @ResponseBody
    public ResponseEntity<?> sendSMS(@RequestParam("phoneNum") String phoneNumber) throws Exception {
        String resultNum = adminService.sendAuthorizationCode(phoneNumber);
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

    @GetMapping("/dashboard")
    public void dashboard() {}

    @GetMapping("/accountManagement/adminInfo")
    public void adminInfo() {}

    @GetMapping("/accountManagement/memberInfo")
    public void memberInfo() {}

    @GetMapping("/companyInfo")
    public void companyInfo() {}

    @GetMapping("/communityManagement")
    public void communityManagement() {}

    @GetMapping("/statistics")
    public void statistics() {}
}
