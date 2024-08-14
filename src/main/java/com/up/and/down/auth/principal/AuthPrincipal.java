package com.up.and.down.auth.principal;

import com.up.and.down.user.User;
import com.up.and.down.user.admin.entity.Admin;
import com.up.and.down.user.member.entity.Member;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Data
@Slf4j
public class AuthPrincipal implements UserDetails, Serializable {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Member#authorities:Set<Authority> -> List<SimpleGrantedAuthority>
        // SimpleGrantedAuthority는 String을 인자로 하는 생성자 제공.
        return user.getAuthorities().stream()
                .map((authority) -> new SimpleGrantedAuthority(authority.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        if (user instanceof Admin) {
            return ((Admin) user).getEmpNum();
        }
        return user.getUserId();
    }

    /**
     * 계정이 만료되지 않았는가?
     * - true(만료되지 않음)
     * - false(만료됨)
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨있지 않은가?
     * - true(잠겨있지 않음)
     * - false(잠김)
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호가 만료되지 않았는가?
     * - true(만료되지 않음)
     * - false(만료됨)
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화 되었는가?
     * - true(활성화)
     * - false(비활성화)
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
