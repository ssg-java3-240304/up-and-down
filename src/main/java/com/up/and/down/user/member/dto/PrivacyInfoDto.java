package com.up.and.down.user.member.dto;

import com.up.and.down.user.member.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyInfoDto {
    private String name;
    private String gender;
    private String birthdate;
    private String email;
    private String phone;
}
