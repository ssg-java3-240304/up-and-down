package com.up.and.down.user.member.entity;

import com.up.and.down.user.Authority;
import com.up.and.down.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "tbl_sns_member")
@Data
@DiscriminatorValue("sns_member")
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class SNSMember extends User {
    private String snsId;
    private String nickname; // 닉네임
    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별
    private LocalDate birth; // 생년월일
    private Marketing marketing; // 마케팅 정보 수신 동의 여부

    @Override
    public String getUserId() {
        return snsId;
    }

    public SNSMember(Long id, String name, String userId, String password, String phone, String email, Set<Authority> authorities, String snsId, String nickname, Gender gender, LocalDate birth, Marketing marketing) {
        super(id, name, userId, password, phone, email, authorities);
        this.snsId = snsId;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.marketing = marketing;
    }

    public void setSNSMemberAuthority() {
        super.setAuthorities(Set.of(Authority.MEMBER));
    }
}
