package com.up.and.down.user.member.entity;

import com.up.and.down.user.Authority;
import com.up.and.down.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "tbl_member")
@Data
@DiscriminatorValue("member")
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Member extends User {
    private String memberId;
    private String nickname; // 닉네임
    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별
    private LocalDate birth; // 생년월일
    private Marketing marketing; // 마케팅 정보 수신 동의 여부

    public Member(Long id, String name, String userId, String password, String phone, String email, Set<Authority> authorities, String memberId, String nickname, com.up.and.down.user.member.entity.Gender gender, LocalDate birth, Marketing marketing) {
            super(id, name, userId, password, phone, email, authorities);
            this.nickname = nickname;
            this.gender = gender;
            this.birth = birth;
            this.marketing = marketing;
    }

    public void setMemberAuthority() {
        super.setAuthorities(Set.of(Authority.MEMBER));
    }
}
