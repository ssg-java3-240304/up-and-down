package com.up.and.down.user.member.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.up.and.down.user.member.entity.Gender;
import com.up.and.down.user.member.entity.SNS;
import com.up.and.down.user.member.entity.SNSMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoProfileDto {
    private String id; // 회원번호
    private String nickname; // 사용자 별명
    private String email; // 사용자 메일 주소

    public void toKakaoProfile(String kakaoUserInfo) {
        JsonElement element = JsonParser.parseString(kakaoUserInfo);
        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
        JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

        this.id = element.getAsJsonObject().get("id").getAsString();
        this.nickname = properties.getAsJsonObject().get("nickname").getAsString();
        this.email = kakaoAccount.getAsJsonObject().get("email").getAsString();
    }

    public SNSMember toKakaoMember() {
        return new SNSMember(
                null,
                null,
                this.id,
                null,
                null,
                this.email,
                null,
                this.id,
                this.nickname,
                null,
                null,
                null,
                SNS.KAKAO);
    }
}
