package com.up.and.down.user.member.service;

import com.up.and.down.user.member.dto.MemberAccountInfoDto;
import com.up.and.down.user.member.dto.MemberDto;
import com.up.and.down.user.member.dto.PrivacyInfoDto;
import com.up.and.down.user.member.entity.Member;
import com.up.and.down.user.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import net.nurigo.java_sdk.exceptions.CoolsmsSystemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberDto memberDto = new MemberDto();

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    public String sendAuthorizationCode(String to) throws CoolsmsException {
        // 인증 번호 생성
        String authorizationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);

        Message coolsms = new Message(apiKey, apiSecretKey);

        HashMap<String, String> params = new HashMap<>();
        params.put("to", to);
        params.put("from", "01021615694");
//        params.put("type", "SMS");
        params.put("text", "[UpAndDown]" + "\n" + "인증번호[" + authorizationCode + "] 타인에게 절대 알려주지 마세요.");

        // 임시 주석 처리
//        try {
//            coolsms.send(params);
//            return authorizationCode;
//        } catch (CoolsmsSystemException e) {
//            e.printStackTrace();
//            return "false";
//        }
        return authorizationCode;
    }

    public void insertPrivacy(PrivacyInfoDto privacyInfoDto) {
        memberDto.insertPrivacy(privacyInfoDto);
    }

    public void insertMemberAccount(MemberAccountInfoDto memberAccountInfoDto) {
        memberDto.insertMemberAccount(memberAccountInfoDto);
    }

    public Optional<Member> findById(Long id){
        return memberRepository.findById(id);
    }
    public void register() {
        Member member = memberDto.toMember();
        log.info("member = {}", member);
        member.setMemberAuthority();
        log.info("member = {}", member);
        memberRepository.save(member);
    }
}
