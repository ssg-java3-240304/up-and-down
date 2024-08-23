package com.up.and.down.user.admin.controller;

import com.up.and.down.user.admin.dto.*;
import com.up.and.down.user.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    // 관리자 로그인 페이지 호출
    @GetMapping("/login")
    public String adminLogin() {
        return "login/adminLogin";
    }

    // 관리자 등록 페이지 호출
    @GetMapping("/register")
    public String adminRegist() {
        return "join/admin/adminAccountInfo";
    }

    // 관리자 등록
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

    // 관리자 로그인 - 관리자 사번 체크
    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<?> check(@RequestParam String empNum) {
       String result = adminService.checkEmpNum(empNum);
        return ResponseEntity.ok(Map.of(
                "result", result
        ));
    }

    // 관리자 등록 - SMS 인증
    @PostMapping("/sign/send")
    @ResponseBody
    public ResponseEntity<?> sendSMS(@RequestParam("phoneNum") String phoneNumber) {
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

    // 대시보드 페이지 호출
    @GetMapping("/dashboard")
    public void dashboard() {}

    // 관리자 정보 관리 페이지 호출
    @GetMapping("/accountManagement/adminInfo")
    public void adminInfo() {}

    // 회원 정보 관리 페이지 호출
    @GetMapping("/accountManagement/memberInfo")
    public void memberInfo() {}

    // 본사 정보 관리 페이지 호출
    @GetMapping("/companyInfo")
    public void companyInfo(Model model) {
        AdminCompanyInfoDto adminCompanyInfo = adminService.getAdminInfo();
        model.addAttribute("adminCompanyInfo", adminCompanyInfo);
    }

    // 커뮤니티 관리 페이지 호출
    @GetMapping("/communityManagement")
    public void communityManagement() {}

    // 접송량 통계 페이지 호출
    @GetMapping("/stat/loginInfo")
    public String loginInfo(Model model) {
        // 접속률 조회(6개월)
        List<VisitCountDto> loginInfoList = adminService.getVisitCountsForLastSixMonths();
        List<BrowserCountDto> browserCounts = adminService.findBrowserCounts();
        log.info("loginInfoList = {}", loginInfoList);
        model.addAttribute("loginInfoList", loginInfoList);
        model.addAttribute("browserCounts", browserCounts);
        return "admin/stat/loginInfo";
    }

    // 통계 - 날짜 지정 조회
    @PostMapping("/stat/search")
    @ResponseBody
    public ResponseEntity<?> fetchData(@RequestBody DateRangeDto dateRangeDto) {
        log.info("dateRangeDto = {}", dateRangeDto);

        // 날짜 범위를 바탕으로 데이터 조회
        List<VisitCountDto> datesData = adminService.getVisitCounts(dateRangeDto.getStartDate().atStartOfDay(), dateRangeDto.getEndDate().atStartOfDay());
        List<BrowserCountDto> browsersData = adminService.getBrowserCounts(dateRangeDto.getStartDate().atStartOfDay(), dateRangeDto.getEndDate().atStartOfDay());
        log.info("datesData = {}", datesData);
        log.info("browsersData = {}", browsersData);

        return ResponseEntity.ok(Map.of(
                "datesData", datesData,
                "browsersData", browsersData
        ));
    }

    // 상품 통계 페이지 호출
    @GetMapping("/stat/productInfo")
    public String productInfo(Model model) {
        log.info("------------------------- productInfo ------------------------------------");
        List<ProductDestinationInfo> destinationNViewCount = adminService.getDestinationNViewCount();
        log.info("------------------------- destinationNViewCount ------------------------------------");
        log.info("destinationNViewCount = {}", destinationNViewCount);
        model.addAttribute("destinationNViewCount", destinationNViewCount);
        return "admin/stat/productInfo";
    }
}
