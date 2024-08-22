package com.up.and.down.user.admin.dto;

import com.up.and.down.user.admin.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private String empNum;
    private String password;
    private String phone;

//    public Admin toAdmin() {
//        return new Admin(
//                null,
//                null,
//                null,
//                this.password,
//                this.phone,
//                null,
//                null,
//                this.username,
//                null,
//                null,
//                null,
//                null
//        );
//    }
}
