package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.DangKy;
import com.example.studentactivity.entity.HoatDong;
import com.example.studentactivity.entity.KhieuNai;
import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.exception.BusinessException;
import com.example.studentactivity.repository.*;
import com.example.studentactivity.service.KhieuNaiService;
import com.example.studentactivity.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KhieuNaiServiceImpl implements KhieuNaiService {

    @Autowired private KhieuNaiRepository khieuNaiRepository;
    @Autowired private SinhVienRepository sinhVienRepository;
    @Autowired private HoatDongRepository hoatDongRepository;
    @Autowired private DiemDanhRepository  diemDanhRepository;
    @Autowired private DangKyRepository dangKyRepository;
    @Autowired private SinhVienService sinhVienService;

    @Override
    public KhieuNai create(Integer activityId, String noiDung) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien sv = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new BusinessException("Không tìm thấy sinh viên"));

        HoatDong hd = hoatDongRepository.findById(activityId)
                .orElseThrow(() -> new BusinessException("Hoạt động không tồn tại"));

        // 1. Chỉ cho khiếu nại sau khi hoạt động kết thúc
        if (hd.getThoiGianKt().isAfter(LocalDateTime.now())) {
            throw new BusinessException("Chỉ có thể khiếu nại sau khi hoạt động đã kết thúc.");
        }

        // 2. Kiểm tra đã đăng ký chưa
        dangKyRepository.findBySinhVienIdAndHoatDongId(sv.getId(), activityId)
                .orElseThrow(() -> new BusinessException("Bạn chưa đăng ký hoạt động này."));

        // 3. Kiểm tra check-in / check-out từ bảng diem_danh
        var diemDanh = diemDanhRepository
                .findBySinhVienIdAndHoatDongId(sv.getId(), activityId)
                .orElseThrow(() -> new BusinessException("Bạn chưa tham gia hoạt động này."));

        if (diemDanh.getCheckIn() == null) {
            throw new BusinessException("Bạn chưa check-in hoạt động này.");
        }

        if (diemDanh.getCheckOut() == null) {
            throw new BusinessException("Bạn chưa check-out hoạt động này.");
        }

        // 4. Tạo khiếu nại
        KhieuNai k = new KhieuNai();
        k.setSinhVien(sv);
        k.setHoatDong(hd);
        k.setNoiDung(noiDung);
        k.setTrangThai("Chờ xử lý");

        return khieuNaiRepository.save(k);
    }

    @Override
    public List<KhieuNai> getMyComplaints() {
        SinhVien sv = sinhVienService.getCurrentUser();
        return khieuNaiRepository.findBySinhVienId(sv.getId());
    }

    @Override
    public List<KhieuNai> getAll(String status) {
        if (status == null || status.isBlank()) {
            return khieuNaiRepository.findAll();
        }
        return khieuNaiRepository.findByTrangThai(status);
    }

    @Override
    public KhieuNai handle(Integer id, String trangThai, String phanHoi) {
        KhieuNai k = khieuNaiRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy khiếu nại"));

        k.setTrangThai(trangThai);
        k.setPhanHoi(phanHoi);

        return khieuNaiRepository.save(k);
    }
}

