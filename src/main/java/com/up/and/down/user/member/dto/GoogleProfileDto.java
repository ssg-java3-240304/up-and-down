package com.up.and.down.user.member.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.up.and.down.user.member.entity.SNS;
import com.up.and.down.user.member.entity.SNSMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleProfileDto {
    private String id; // 회원번호
    private String name; // 사용자 별명
    private String email; // 사용자 메일 주소

    public void toGoogleProfile(String googleUserInfo) {
        JsonElement element = JsonParser.parseString(googleUserInfo);

        this.id = element.getAsJsonObject().get("id").getAsString();
        this.name = element.getAsJsonObject().get("name").getAsString();
        this.email = element.getAsJsonObject().get("email").getAsString();
    }

    public SNSMember toGoogleMember() {
        return new SNSMember(
                null,
                this.name,
                this.id,
                null,
                null,
                this.email,
                null,
                this.id,
                null,
                null,
                null,
                null,
                SNS.GOOGLE);
    }
}
