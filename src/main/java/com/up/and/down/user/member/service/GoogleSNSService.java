package com.up.and.down.user.member.service;

import com.up.and.down.user.member.dto.GoogleProfileDto;
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

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GoogleSNSService {
    private final SNSMemberRepository snsMemberRepository;

    @Value("${sns.google.key}")
    private String googleKey;
    @Value("${sns.google.secret}")
    private String googleSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String reqSnsApi() {
        String clientId = googleKey;//애플리케이션 클라이언트 아이디값";
//        String redirectURI = "http://localhost:8080/app/member/google_login";
        String redirectURI = "http://175.45.205.36:8080/app/member/google_login";
        String apiURL = "https://accounts.google.com/o/oauth2/v2/auth?";
        apiURL += "client_id=" + clientId;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&response_type=code&scope=email profile";
        return apiURL;
    }

    public String getGoogleAccessToken(String code) {
        URI uri = UriComponentsBuilder.fromUriString("https://oauth2.googleapis.com/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", googleKey)
                .queryParam("client_secret", googleSecret)
                .queryParam("code", code)
//                .queryParam("redirect_uri", "http://localhost:8080/app/member/google_login")
                .queryParam("redirect_uri", "http://175.45.205.36:8080/app/member/google_login")
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

    public GoogleProfileDto getUserInfo(String accessToken) {
        String reqUrl = "https://www.googleapis.com/userinfo/v2/me";

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader 오브젝트
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(reqUrl,
                HttpMethod.GET,
                googleProfileRequest,
                String.class);

//        log.info("response = " + response);
        GoogleProfileDto googleProfile = new GoogleProfileDto();
        googleProfile.toGoogleProfile(response.getBody());

        return googleProfile;
    }

    public void saveGoogleMember(GoogleProfileDto userInfo) {
        SNSMember snsMember = userInfo.toGoogleMember();
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
