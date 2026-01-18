package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.repository.SinhVienRepository;
import com.example.studentactivity.security.CustomUserDetails;
import com.example.studentactivity.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SinhVienServiceImpl implements SinhVienService {

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Override
    public SinhVien getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return null;
        }

        String mssv = userDetails.getUsername();

        return sinhVienRepository.findByMssv(mssv)
                .orElse(null);
    }

    @Override
    public List<SinhVien> getByLopId(Integer lopId) {
        return sinhVienRepository.findByLopIdWithRoles(lopId);
    }

}
