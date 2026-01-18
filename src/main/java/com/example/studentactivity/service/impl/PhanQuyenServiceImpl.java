package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.PhanQuyen;
import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.entity.VaiTro;
import com.example.studentactivity.repository.PhanQuyenRepository;
import com.example.studentactivity.repository.SinhVienRepository;
import com.example.studentactivity.repository.VaiTroRepository;
import com.example.studentactivity.service.PhanQuyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class PhanQuyenServiceImpl implements PhanQuyenService {

    @Autowired
    private PhanQuyenRepository phanQuyenRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Override
    @Transactional
    public PhanQuyen assignRole(
            Integer userId,
            Integer vaiTroId,
            String hocKy,
            String namHoc,
            LocalDate ngayBatDau,
            LocalDate ngayKetThuc
    ) {
        // 1) XÓA HẾT role cũ của sinh viên
        phanQuyenRepository.deleteBySinhVienId(userId);

        // 2) LẤY SV + ROLE
        SinhVien sv = sinhVienRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        VaiTro role = vaiTroRepository.findById(vaiTroId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        // 3) TẠO role mới
        PhanQuyen pq = new PhanQuyen();
        pq.setSinhVien(sv);
        pq.setVaiTro(role);
        pq.setHocKy(hocKy);
        pq.setNamHoc(namHoc);
        pq.setNgayBatDau(ngayBatDau);
        pq.setNgayKetThuc(ngayKetThuc);

        return phanQuyenRepository.save(pq);
    }


    @Override
    @Transactional
    public void removeRole(Integer userId, Integer roleId) {
        phanQuyenRepository.deleteBySinhVienIdAndVaiTroId(userId, roleId);
    }
}
