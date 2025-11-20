package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.PhanQuyen;
import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.repository.PhanQuyenRepository;
import com.example.studentactivity.repository.SinhVienRepository;
import com.example.studentactivity.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired private PhanQuyenRepository phanQuyenRepository;
    @Autowired private SinhVienRepository sinhVienRepository;

    @Override
    public List<PhanQuyen> getActiveRoles(Integer sinhVienId) {
        return phanQuyenRepository.findActiveRolesBySinhVienId(sinhVienId, LocalDate.now());
    }

    @Override
    public boolean hasAuthority(String authority) {
        String mssv = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien currentUser = sinhVienRepository.findByMssv(mssv)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên hiện tại"));

        return getActiveRoles(currentUser.getId()).stream()
                .anyMatch(pq -> pq.getVaiTro().getTenVaiTro().equals(authority));
    }

    // Thêm method tiện ích (dùng nhiều nơi)
    public Integer getCurrentSinhVienId() {
        String mssv = SecurityContextHolder.getContext().getAuthentication().getName();
        return sinhVienRepository.findByMssv(mssv)
                .map(SinhVien::getId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
    }
}