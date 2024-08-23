package com.up.and.down.user.member.dto;

import com.up.and.down.user.member.entity.Gender;
import com.up.and.down.user.member.entity.Marketing;
import com.up.and.down.user.member.entity.Member;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String name;
    private String userId;
    private String password;
    private String phone;
    private String email;
    private String nickname;
    private Gender gender;
    private LocalDate birth;
    private Marketing marketing;

    public Member toMember() {
        return new Member(
                null,
                this.name,
                this.userId,
                this.password,
                this.phone,
                this.email,
                null,
                this.userId,
                this.nickname,
                this.gender,
                this.birth,
                this.marketing);
    }

    public void insertPrivacy(PrivacyInfoDto privacyInfoDto) {
        this.name = privacyInfoDto.getName();
        if(privacyInfoDto.getGender().equals("male")) {
            this.gender = Gender.MALE;
        } else {
            this.gender = Gender.FEMALE;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.birth = LocalDate.parse(privacyInfoDto.getBirthdate(), formatter);
        this.email = privacyInfoDto.getEmail();
        this.phone = privacyInfoDto.getPhone();
    }

    public void insertMemberAccount(MemberAccountInfoDto memberAccountInfoDto) {
        this.userId = memberAccountInfoDto.getUsername();
        this.password = memberAccountInfoDto.getPassword();
        this.nickname = memberAccountInfoDto.getNickname();
    }
}