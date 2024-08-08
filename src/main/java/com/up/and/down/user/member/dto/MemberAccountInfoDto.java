package com.up.and.down.user.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberAccountInfoDto {
    private String username;
    private String password;
    private String nickname;
}
