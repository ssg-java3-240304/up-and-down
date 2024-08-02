package com.up.and.down.member.entity;

import lombok.*;

import java.time.LocalDate;

//@Entity
//@Table(name = "tbl_member")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
//    @Id
    private Long id;
    private String memberId;
    private String password;
    private String nickname;
    private String userName;
    private Gender Gender;
    private LocalDate birth;
    private String phone;
    private String email;
    private Marketing marketing;
}
