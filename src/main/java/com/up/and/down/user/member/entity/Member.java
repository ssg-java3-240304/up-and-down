package com.up.and.down.user.member.entity;

import com.up.and.down.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_member")
@Data
@DiscriminatorValue("member")
@EqualsAndHashCode(callSuper = false)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends User {
    private String nickname; // 닉네임
    private Gender Gender; // 성별
    private LocalDate birth; // 생년월일
    private Marketing marketing; // 마케팅 정보 수신 동의 여부
}
