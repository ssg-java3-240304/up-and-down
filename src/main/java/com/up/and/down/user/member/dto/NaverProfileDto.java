package com.up.and.down.user.member.dto;

import com.google.gson.JsonElement;
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
public class NaverProfileDto {
    private String id; // 동일인 식별 정보
    private String nickname; // 사용자 별명
    private String name; // 사용자 이름
    private String email; // 사용자 메일 주소
    private String phone; // 핸드폰 번호
    private Gender gender; // 성별 F: 여성, M: 남성, U: 확인불가
    private LocalDate birth; // 사용자 생일

    public void toNaverProfile(String jsonResponseBody)
    {
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonResponseBody);

        this.id = element.getAsJsonObject().get("response").getAsJsonObject().get("id").getAsString();
        this.nickname = element.getAsJsonObject().get("response").getAsJsonObject().get("nickname").getAsString();
        this.name = element.getAsJsonObject().get("response").getAsJsonObject().get("name").getAsString();
        this.email = element.getAsJsonObject().get("response").getAsJsonObject().get("email").getAsString();
        this.phone = element.getAsJsonObject().get("response").getAsJsonObject().get("mobile").getAsString();
        if(element.getAsJsonObject().get("response").getAsJsonObject().get("gender").getAsString().equals("M")) {
            this.gender = Gender.MALE;
        } else {
            this.gender = Gender.FEMALE;
        }
        String fullDate = element.getAsJsonObject().get("response").getAsJsonObject().get("birthyear").getAsString()
                    + "-"
                    + element.getAsJsonObject().get("response").getAsJsonObject().get("birthday").getAsString();
        this.birth = LocalDate.parse(fullDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public SNSMember toSNSMember() {
        return new SNSMember(
                null,
                this.name,
                this.id,
                null,
                this.phone,
                this.email,
                null,
                this.id,
                this.nickname,
                this.gender,
                this.birth,
                null,
                SNS.NAVER);
    }
}
