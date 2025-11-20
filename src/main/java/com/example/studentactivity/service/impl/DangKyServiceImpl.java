package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.*;
import com.example.studentactivity.exception.BusinessException;
import com.example.studentactivity.repository.*;
import com.example.studentactivity.service.DangKyService;
import com.example.studentactivity.service.HoatDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DangKyServiceImpl implements DangKyService {

    @Autowired private DangKyRepository dangKyRepository;
    @Autowired private HoatDongRepository hoatDongRepository;
    @Autowired private SinhVienRepository sinhVienRepository;
    @Autowired private HoatDongService hoatDongService;
    @Autowired private DiemDanhRepository diemDanhRepository;

    @Override
    @Transactional
    public DangKy register(Integer hoatDongId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien sv = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new BusinessException("Sinh viên không tồn tại"));

        HoatDong hd = hoatDongRepository.findById(hoatDongId)
                .orElseThrow(() -> new BusinessException("Hoạt động không tồn tại"));

        if (!"Mở".equals(hd.getTrangThai())) {
            throw new BusinessException("Hoạt động đã đóng đăng ký");
        }

        if (dangKyRepository.findBySinhVienIdAndHoatDongId(sv.getId(), hoatDongId).isPresent()) {
            throw new BusinessException("Bạn đã đăng ký hoạt động này rồi");
        }

        if (hoatDongService.isFull(hoatDongId)) {
            throw new BusinessException("Hoạt động đã đủ số lượng, không thể đăng ký thêm");
        }

        DangKy dk = new DangKy();
        dk.setSinhVien(sv);
        dk.setHoatDong(hd);
        dk.setThoiDiemDk(LocalDateTime.now());
        dk.setTrangThaiDk("Đã đăng ký");

        DangKy saved = dangKyRepository.save(dk);

        // Sau khi thêm xong, nếu vừa đủ thì đổi trạng thái hoạt động
        if (hoatDongService.isFull(hoatDongId) && "Mở".equals(hd.getTrangThai())) {
            hd.setTrangThai("Đã đầy");
            hoatDongRepository.save(hd);
        }

        return saved;
    }

    @Override
    public List<DangKy> getMyRegistrations() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien sv = sinhVienRepository.findByMssv(username).orElseThrow();

        List<DangKy> list = dangKyRepository.findBySinhVienId(sv.getId());

        // Gắn trạng thái checkin/checkout cho từng đăng ký
        for (DangKy dk : list) {
            diemDanhRepository.findBySinhVienIdAndHoatDongId(
                    sv.getId(),
                    dk.getHoatDong().getId()
            ).ifPresent(dd -> {
                dk.setDaCheckIn(dd.getCheckIn() != null);
                dk.setDaCheckOut(dd.getCheckOut() != null);
            });
        }

        return list;
    }

    @Override
    public List<DangKy> getByHoatDongId(Integer hoatDongId) {
        return dangKyRepository.findByHoatDongId(hoatDongId);
    }
}
