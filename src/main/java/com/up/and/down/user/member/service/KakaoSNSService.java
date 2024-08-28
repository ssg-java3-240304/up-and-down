package com.up.and.down.user.member.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.up.and.down.user.member.dto.KakaoProfileDto;
import com.up.and.down.user.member.entity.SNSMember;
import com.up.and.down.user.member.repository.SNSMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KakaoSNSService {
    private final SNSMemberRepository snsMemberRepository;
    private final KakaoProfileDto kakaoProfile = new KakaoProfileDto();

    @Value("${sns.kakao.key}")
    private String kakaoKey;
    @Value("${sns.kakao.secret}")
    private String kakaoSecret;

    public String reqSnsApi() {
        String clientId = kakaoKey;//애플리케이션 클라이언트 아이디값";
//        String redirectURI = "http://localhost:8080/member/kakao_login";
        String redirectURI = "http://211.188.54.220:8080/member/kakao_login";
        String apiURL = "https://kauth.kakao.com/oauth/authorize?response_type=code";
        apiURL += "&client_id=" + clientId;
        apiURL += "&redirect_uri=" + redirectURI;
        return apiURL;
    }

    public String getKakaoAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String requestURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            // setDoOutput()은 OutputStream으로 POST 데이터를 넘겨 주겠다는 옵션이다.
            // POST 요청을 수행하려면 setDoOutput()을 true로 설정한다.
            conn.setDoOutput(true);

            // POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + kakaoKey +// REST_API_KEY
//                    "&redirect_uri=http://localhost:8080/member/kakao_login" + // REDIRECT_URI
                    "&redirect_uri=http://211.188.54.220:8080/member/kakao_login" + // REDIRECT_URI
                    "&code=" + code +
                    "&client_secret=" + kakaoSecret;
            bufferedWriter.write(sb);
            bufferedWriter.flush();

            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            // 요청을 통해 얻은 데이터를 InputStreamReader을 통해 읽어 오기
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            log.info("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("accessToken = {}", accessToken);
            log.info("refreshToken = {}", refreshToken);

            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public KakaoProfileDto getUserInfo(String accessToken) {
        String postURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(postURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            log.info("responseCode = {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            log.info("response body = {}", result);

            kakaoProfile.toKakaoProfile(result.toString());

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return kakaoProfile;
    }

    public void saveKakaoMember(KakaoProfileDto userInfo) {
        SNSMember snsMember = userInfo.toKakaoMember();
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
