// src/main/java/com/example/studentactivity/security/CustomUserDetailsService.java

package com.example.studentactivity.security;

import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.entity.PhanQuyen;
import com.example.studentactivity.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Override
    public UserDetails loadUserByUsername(String mssv) throws UsernameNotFoundException {
        SinhVien sv = sinhVienRepository.findByMssv(mssv)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy sinh viên: " + mssv));

        List<PhanQuyen> phanQuyenList = sinhVienRepository.findPhanQuyenBySinhVienIdAndActive(
                sv.getId(), LocalDate.now());

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (phanQuyenList == null || phanQuyenList.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SinhVien"));
        } else {
            for (PhanQuyen pq : phanQuyenList) {
                String role = "ROLE_" + pq.getVaiTro().getTenVaiTro();
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }

        return new CustomUserDetails(sv, authorities);
    }

}