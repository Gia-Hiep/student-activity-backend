package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.*;
import com.example.studentactivity.exception.BusinessException;
import com.example.studentactivity.repository.*;
import com.example.studentactivity.service.HoatDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HoatDongServiceImpl implements HoatDongService {

    @Autowired private HoatDongRepository hoatDongRepository;
    @Autowired private DangKyRepository dangKyRepository;
    @Autowired private SinhVienRepository sinhVienRepository;

    @Override
    public List<HoatDong> getAll(Integer khoaId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isStudent = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_Sinh viên"));

        if (isStudent) {
            String username = auth.getName();
            SinhVien sv = sinhVienRepository.findByMssv(username)
                    .orElseThrow(() -> new BusinessException("Không tìm thấy sinh viên"));

            return hoatDongRepository.findByCapToChucAndLopId("LOP", sv.getLop().getId());
        }
        boolean isClassLeader = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_Cán sự lớp")
                        || a.getAuthority().equals("ROLE_Liên chi hội"));

        if (isClassLeader) {
            String username = auth.getName();
            SinhVien sv = sinhVienRepository.findByMssv(username)
                    .orElseThrow(() -> new BusinessException("Không tìm thấy sinh viên"));

            return hoatDongRepository.findByCapToChucAndLopId("LOP", sv.getLop().getId());
        }

        List<HoatDong> list;

        if (khoaId != null) {
            list = hoatDongRepository.findByKhoaId(khoaId);
        } else {
            list = hoatDongRepository.findAll();
        }

        updateStatusForEndedActivities(list);

        return list;
    }

    public HoatDong createFacultyActivity(HoatDong hoatDong) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien creator = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new BusinessException("Không tìm thấy tài khoản"));

        hoatDong.setNguoiTao(creator);
        hoatDong.setCapToChuc("KHOA");
        hoatDong.setParent(null);
        hoatDong.setKhoa(creator.getLop().getKhoa());
        hoatDong.setTrangThai("Mở");

        validateThoiGian(hoatDong);

        return hoatDongRepository.save(hoatDong);
    }

    @Transactional
    public HoatDong deployFacultyActivityToClass(Integer facultyActivityId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien sv = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new BusinessException("Không tìm thấy sinh viên"));

        Lop lop = sv.getLop();

        HoatDong facultyHd = hoatDongRepository.findById(facultyActivityId)
                .orElseThrow(() -> new BusinessException("Hoạt động khoa không tồn tại"));

        if (!facultyHd.getCapToChuc().equals("KHOA")) {
            throw new BusinessException("Không thể triển khai hoạt động không phải cấp Khoa");
        }

        boolean existed = hoatDongRepository.existsByParentIdAndLopId(facultyActivityId, lop.getId());
        if (existed) {
            throw new BusinessException("Lớp đã triển khai hoạt động này rồi");
        }

        HoatDong classHd = new HoatDong();
        classHd.setTenHd(facultyHd.getTenHd());
        classHd.setMoTa(facultyHd.getMoTa());
        classHd.setDiaDiem(facultyHd.getDiaDiem());
        classHd.setThoiGianBd(facultyHd.getThoiGianBd());
        classHd.setThoiGianKt(facultyHd.getThoiGianKt());
        classHd.setGioiHanSv(facultyHd.getGioiHanSv());

        classHd.setCapToChuc("LOP");
        classHd.setParent(facultyHd);
        classHd.setLop(lop);
        classHd.setKhoa(facultyHd.getKhoa());
        classHd.setNguoiTao(sv);
        classHd.setTrangThai("Mở");

        validateThoiGian(classHd);

        return hoatDongRepository.save(classHd);
    }

    private void updateStatusForEndedActivities(List<HoatDong> list) {
        LocalDateTime now = LocalDateTime.now();
        boolean changed = false;

        for (HoatDong hd : list) {
            if (hd.getThoiGianKt() != null
                    && hd.getThoiGianKt().isBefore(now)
                    && !"Đã kết thúc".equals(hd.getTrangThai())) {

                hd.setTrangThai("Đã kết thúc");
                changed = true;
            }
        }

        if (changed) {
            hoatDongRepository.saveAll(list);
        }
    }

    @Override
    public HoatDong getById(Integer id) {
        HoatDong hd = hoatDongRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Hoạt động không tồn tại"));

        LocalDateTime now = LocalDateTime.now();
        if (hd.getThoiGianKt() != null
                && hd.getThoiGianKt().isBefore(now)
                && !"Đã kết thúc".equals(hd.getTrangThai())) {
            hd.setTrangThai("Đã kết thúc");
            hoatDongRepository.save(hd);
        }

        return hd;
    }

    // HÀM CHECK THỜI GIAN
    private void validateThoiGian(HoatDong hoatDong) {
        LocalDateTime start = hoatDong.getThoiGianBd();
        LocalDateTime end   = hoatDong.getThoiGianKt();

        if (start == null || end == null) {
            throw new BusinessException("Vui lòng chọn đầy đủ thời gian bắt đầu và kết thúc");
        }

        if (!end.isAfter(start)) {
            throw new BusinessException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }

        LocalDateTime now = LocalDateTime.now();
        if (start.isBefore(now)) {
            throw new BusinessException("Thời gian bắt đầu phải lớn hơn hoặc bằng thời điểm hiện tại");
        }
    }

    @Override
    @Transactional
    public HoatDong create(HoatDong hoatDong) {
        validateThoiGian(hoatDong);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien creator = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        hoatDong.setNguoiTao(creator);
        hoatDong.setTrangThai("Mở");
        return hoatDongRepository.save(hoatDong);
    }

    @Override
    @Transactional
    public HoatDong update(Integer id, HoatDong hoatDong) {
        HoatDong existing = getById(id);

        existing.setTenHd(hoatDong.getTenHd());
        existing.setMoTa(hoatDong.getMoTa());
        existing.setLoai(hoatDong.getLoai());
        existing.setThoiGianBd(hoatDong.getThoiGianBd());
        existing.setThoiGianKt(hoatDong.getThoiGianKt());
        existing.setDiaDiem(hoatDong.getDiaDiem());
        existing.setGioiHanSv(hoatDong.getGioiHanSv());

        validateThoiGian(existing);

        return hoatDongRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        HoatDong hd = getById(id);
        hoatDongRepository.delete(hd);
    }

    private boolean isFullInternal(HoatDong hoatDong) {
        if (hoatDong.getGioiHanSv() == null || hoatDong.getGioiHanSv() <= 0) {
            return false; // không giới hạn
        }
        long current = dangKyRepository.countByHoatDongId(hoatDong.getId());
        return current >= hoatDong.getGioiHanSv();
    }

    @Override
    public boolean isFull(Integer hoatDongId) {
        HoatDong hd = getById(hoatDongId);
        return isFullInternal(hd);
    }
}
