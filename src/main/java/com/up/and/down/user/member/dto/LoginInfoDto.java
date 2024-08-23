package com.up.and.down.user.member.dto;

import com.up.and.down.user.member.entity.LoginInfo;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoDto {
    private String userId;
    private LocalDateTime loginTime;
    private String browserInfo;

    public LoginInfo toLoginInfo() {
        return new LoginInfo(null, this.userId, this.loginTime, this.browserInfo);
    }
}
