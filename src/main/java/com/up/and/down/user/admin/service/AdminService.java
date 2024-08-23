package com.up.and.down.user.admin.service;

import com.up.and.down.user.admin.dto.*;
import com.up.and.down.user.admin.entity.Admin;
import com.up.and.down.user.admin.entity.Company;
import com.up.and.down.user.admin.repository.AdminRepository;
import com.up.and.down.user.admin.repository.CompanyRepository;
import com.up.and.down.user.admin.repository.ProductStatRepository;
import com.up.and.down.user.member.repository.LoginInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {
    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final LoginInfoRepository loginInfoRepository;
    private final ProductStatRepository productStatRepository;

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    // SMS 인증번호 요청
    public String sendAuthorizationCode(String phoneNumber) {
        // 인증 번호 생성
        String authorizationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);

        Message coolsms = new Message(apiKey, apiSecretKey);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", phoneNumber);
        params.put("from", "01021615694");
//        params.put("type", "SMS");
        params.put("text", "[UpAndDown]" + "\n" + "관리자 인증번호[" + authorizationCode + "] 타인에게 절대 알려주지 마세요.");

        // 임시 주석 처리
        try {
            coolsms.send(params);
            return authorizationCode;
        } catch (CoolsmsException e) {
            e.printStackTrace();
            return "false";
        }
//        return authorizationCode;
    }

    public boolean adminRegister(AdminDto adminDto) {
        // 정보 업데이트를 하기 위해 조회
        Admin admin = adminRepository.findByEmpNum(adminDto.getEmpNum());
        // 정보 업데이트
        admin.changeAdminInfo(adminDto);
        return admin.getPassword().isEmpty();
    }

    public String checkEmpNum(String empNum) {
        Admin admin = adminRepository.findByEmpNum(empNum);
        String result;
        if(admin != null) {
            if(admin.getPassword() == null || admin.getPassword().isEmpty()) {
                result = "empty";
            } else {
                result = "registered";
            }
        } else {
            result = "noInfo";
        }
        return result;
    }

    public AdminCompanyInfoDto getAdminInfo() {
        Company adminCompanyList = companyRepository.findById(1).orElseThrow();
        return AdminCompanyInfoDto.fromAdminCompanyInfo(adminCompanyList);
    }

    public List<VisitCountDto> getVisitCountsForLastSixMonths() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        return loginInfoRepository.findVisitCountsByMonth(sixMonthsAgo);
    }

    public List<BrowserCountDto> findBrowserCounts() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        return loginInfoRepository.findBrowserCounts(sixMonthsAgo);
    }

    public List<VisitCountDto> getVisitCounts(LocalDateTime startDate, LocalDateTime endDate) {
        return loginInfoRepository.getVisitCounts(startDate, endDate);
    }

    public List<BrowserCountDto> getBrowserCounts(LocalDateTime startDate, LocalDateTime endDate) {
        return loginInfoRepository.getBrowserCounts(startDate, endDate);
    }

    public List<ProductDestinationInfo> getDestinationNViewCount() {
        return productStatRepository.getDestinationNViewCount();
    }
}
