package com.up.and.down.user.member.service;

import com.up.and.down.user.member.dto.NaverProfileDto;
import com.up.and.down.user.member.entity.SNSMember;
import com.up.and.down.user.member.repository.SNSMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NaverSNSService {
    private final SNSMemberRepository snsMemberRepository;

    @Value("${sns.naver.key}")
    private String naverKey;
    @Value("${sns.naver.secret}")
    private String naverSecret;
    private final RestTemplate restTemplate = new RestTemplate();


    public String reqSnsApi() throws UnsupportedEncodingException {
        String clientId = naverKey;//애플리케이션 클라이언트 아이디값";
//        String redirectURI = URLEncoder.encode("http://localhost:8080/member/naver_login", "UTF-8");
        String redirectURI = URLEncoder.encode("http://updown.website/member/naver_login", "UTF-8");
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
        apiURL += "&client_id=" + clientId;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&state=" + state;
        return apiURL;
    }

    // naver 접근 토큰 발급 요청
    public String exchangeCodeForAccessToken(String code, String state) {
        // 토큰 발급 URL 구성
        URI uri = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", naverKey)
                .queryParam("client_secret", naverSecret)
                .queryParam("code", code)
                .queryParam("state", state)
                .build()
                .toUri();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        // 요청 엔티티 생성
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // POST 요청 보내기
        ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Map.class);

        // 응답에서 access_token 추출
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("access_token")) {
            return (String) responseBody.get("access_token");
        } else {
            throw new RuntimeException("Failed to retrieve access token from Naver");
        }
    }

    // naver 사용자 프로필 api 호출
    public NaverProfileDto getUserInfo(String accessToken) {
        String reqUrl = "https://openapi.naver.com/v1/nid/me";

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader 오브젝트
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> naverProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(reqUrl,
                HttpMethod.POST,
                naverProfileRequest,
                String.class);

//        log.info("response = " + response);
        NaverProfileDto naverProfile = new NaverProfileDto();
        naverProfile.toNaverProfile(response.getBody());

        return naverProfile;
    }

    // 네이버 로그인 회원 정보 DB 저장
    public void saveNaverMember(NaverProfileDto userInfo) {
        SNSMember snsMember = userInfo.toSNSMember();
        // MEMBER 권한 부여
        snsMember.setSNSMemberAuthority();
        SNSMember member = snsMemberRepository.findByUserId(snsMember.getUserId());
        if(member != null) {
            // 갱신
        } else {
            snsMemberRepository.save(snsMember);
        }
    }
}
