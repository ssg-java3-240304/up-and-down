package com.up.and.down.user.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_login_info")
@Setter(AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "login_time")
    private LocalDateTime loginTime;
    @Column(name = "browser_info")
    private String browserInfo;
}
