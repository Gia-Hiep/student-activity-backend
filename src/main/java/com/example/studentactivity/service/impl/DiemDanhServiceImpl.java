package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.DiemDanh;
import com.example.studentactivity.entity.HoatDong;
import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.exception.BusinessException;
import com.example.studentactivity.repository.DiemDanhRepository;
import com.example.studentactivity.repository.HoatDongRepository;
import com.example.studentactivity.service.DiemDanhService;
import com.example.studentactivity.service.SinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DiemDanhServiceImpl implements DiemDanhService {

    @Autowired
    private DiemDanhRepository diemDanhRepository;

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private HoatDongRepository hoatDongRepository;

    @Override
    @Transactional
    public DiemDanh checkIn(Integer hoatDongId) {

        SinhVien current = sinhVienService.getCurrentUser();

        HoatDong hd = hoatDongRepository.findById(hoatDongId)
                .orElseThrow(() -> new BusinessException("Hoạt động không tồn tại"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = hd.getThoiGianBd();
        LocalDateTime end = hd.getThoiGianKt();

        if (start == null || end == null) {
            throw new BusinessException("Hoạt động chưa cấu hình thời gian");
        }

        LocalDateTime earliestCheckIn = start.minusMinutes(15);  // check-in sớm 15 phút
        LocalDateTime latestCheckIn = end;                       // muộn nhất đến khi kết thúc

        if (now.isBefore(earliestCheckIn)) {
            throw new BusinessException("Chưa tới giờ check-in (được phép check-in trước 15 phút).");
        }

        if (now.isAfter(latestCheckIn)) {
            throw new BusinessException("Đã quá giờ check-in (hoạt động đã kết thúc).");
        }

        DiemDanh diemDanh = diemDanhRepository
                .findBySinhVienIdAndHoatDongId(current.getId(), hoatDongId)
                .orElseGet(() -> {
                    DiemDanh d = new DiemDanh();
                    d.setSinhVien(current);
                    d.setHoatDong(hd);
                    return d;
                });

        if (diemDanh.getCheckIn() != null) {
            throw new BusinessException("Bạn đã check-in rồi");
        }

        diemDanh.setCheckIn(now);
        return diemDanhRepository.save(diemDanh);
    }


    @Override
    @Transactional
    public DiemDanh checkOut(Integer hoatDongId) {

        SinhVien current = sinhVienService.getCurrentUser();

        HoatDong hd = hoatDongRepository.findById(hoatDongId)
                .orElseThrow(() -> new BusinessException("Hoạt động không tồn tại"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = hd.getThoiGianBd();
        LocalDateTime end = hd.getThoiGianKt();

        if (start == null || end == null) {
            throw new BusinessException("Hoạt động chưa cấu hình thời gian");
        }

        LocalDateTime earliestCheckOut = end;                   // chỉ check-out khi kết thúc
        LocalDateTime latestCheckOut = end.plusHours(1);        // cho trễ 1 giờ

        if (now.isBefore(earliestCheckOut)) {
            throw new BusinessException("Hoạt động chưa kết thúc, chưa thể check-out");
        }

        if (now.isAfter(latestCheckOut)) {
            throw new BusinessException("Đã quá giờ check-out (chỉ được trễ tối đa 1 giờ).");
        }

        DiemDanh diemDanh = diemDanhRepository
                .findBySinhVienIdAndHoatDongId(current.getId(), hoatDongId)
                .orElseThrow(() -> new BusinessException("Bạn chưa check-in, không thể check-out"));

        if (diemDanh.getCheckOut() != null) {
            throw new BusinessException("Bạn đã check-out rồi");
        }

        diemDanh.setCheckOut(now);
        return diemDanhRepository.save(diemDanh);
    }

}
