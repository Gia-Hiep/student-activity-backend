package com.example.studentactivity.security;

import com.example.studentactivity.entity.SinhVien;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final String mssv;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;

    // Constructor dùng khi login bằng username/password
    public CustomUserDetails(SinhVien sinhVien, List<GrantedAuthority> authorities) {
        this.mssv = sinhVien.getMssv();
        this.password = sinhVien.getMatKhau();
        this.authorities = authorities;
        this.enabled = "Hoạt động".equals(sinhVien.getTrangThai());
    }

    // Constructor dùng trong JwtAuthenticationFilter (chỉ có mssv + roles từ token)
    public CustomUserDetails(String mssv, List<String> roles) {
        this.mssv = mssv;
        this.password = null;
        this.authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mssv;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}