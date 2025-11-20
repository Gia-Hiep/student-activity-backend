package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.*;
import com.example.studentactivity.exception.BusinessException;
import com.example.studentactivity.repository.*;
import com.example.studentactivity.service.MinhChungService;
import com.example.studentactivity.util.FileCompressor;
import com.example.studentactivity.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MinhChungServiceImpl implements MinhChungService {

    @Autowired private MinhChungRepository minhChungRepository;
    @Autowired private SinhVienRepository sinhVienRepository;
    @Autowired private HoatDongRepository hoatDongRepository;
    @Autowired private QuyDoiDiemRepository quyDoiDiemRepository;
    @Autowired private DiemRenLuyenRepository diemRenLuyenRepository;
    @Autowired private FileCompressor fileCompressor; // nếu chưa dùng thì để đó cũng không sao
    @Autowired private FileUploadUtil fileUploadUtil;

    @Override
    @Transactional
    public MinhChung upload(Integer hoatDongId, MultipartFile file, String ghiChu) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien sv = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new BusinessException("Sinh viên không tồn tại"));

        HoatDong hd = hoatDongRepository.findById(hoatDongId)
                .orElseThrow(() -> new BusinessException("Hoạt động không tồn tại"));

        // ===== KIỂM TRA THỜI GIAN HOẠT ĐỘNG (CHỈ CHO NỘP KHI ĐANG DIỄN RA HOẶC ĐÃ KẾT THÚC) =====
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = hd.getThoiGianBd();
        LocalDateTime end = hd.getThoiGianKt();

        if (start == null || end == null) {
            throw new BusinessException("Hoạt động chưa cấu hình thời gian, không thể nộp minh chứng");
        }

        // Chỉ cho nộp khi now >= start  (đang diễn ra hoặc đã kết thúc)
        if (now.isBefore(start)) {
            throw new BusinessException("Hoạt động chưa diễn ra, chưa thể nộp minh chứng");
        }

         LocalDateTime lastAccept = end.plusHours(3);
         if (now.isAfter(lastAccept)) {
             throw new BusinessException("Đã quá hạn nộp minh chứng cho hoạt động này");
         }

        String fileUrl;
        try {
            fileUrl = fileUploadUtil.saveFile(file);
        } catch (IOException e) {
            throw new BusinessException("Lỗi lưu file minh chứng");
        }

        MinhChung mc = new MinhChung();
        mc.setSinhVien(sv);
        mc.setHoatDong(hd);
        mc.setFileUrl(fileUrl);
        mc.setGhiChu(ghiChu);
        mc.setTrangThaiDuyet("Chờ duyệt");

        return minhChungRepository.save(mc);
    }


    @Override
    public List<MinhChung> getMyEvidences() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien sv = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new BusinessException("Sinh viên không tồn tại"));
        return minhChungRepository.findBySinhVienId(sv.getId());
    }

    @Override
    public List<MinhChung> getPendingForApproval() {
        return minhChungRepository.findByTrangThaiDuyet("Chờ duyệt");
    }

    @Override
    @Transactional
    public MinhChung approve(Integer id) {
        MinhChung mc = minhChungRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Minh chứng không tồn tại"));

        if (!"Chờ duyệt".equals(mc.getTrangThaiDuyet())) {
            throw new BusinessException("Minh chứng đã được xử lý");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien approver = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new BusinessException("Người duyệt không tồn tại"));

        mc.setTrangThaiDuyet("Đã duyệt");
        mc.setNguoiDuyet(approver);
        mc.setNgayDuyet(LocalDateTime.now());

        // ===== TỰ ĐỘNG CỘNG ĐIỂM (nếu dữ liệu đủ) =====
        HoatDong hd = mc.getHoatDong();

        // Nếu hoạt động hoặc loại hoạt động chưa gán -> vẫn duyệt minh chứng, nhưng bỏ qua cộng điểm
        if (hd != null && hd.getLoai() != null) {
            Integer loaiId = hd.getLoai().getId();

            // ví dụ: tạm hard-code tiêu chí C1 (id_tieu_chi = 1)
            QuyDoiDiem qd = quyDoiDiemRepository
                    .findByLoaiHoatDongIdAndTieuChiId(loaiId, 1)
                    .orElseThrow(() -> new BusinessException("Không tìm thấy quy đổi điểm"));

            // TODO: sau này có thể lấy học kỳ/năm học từ cấu hình hoặc từ hoạt động
            String hocKy = "HK1";
            String namHoc = "2025-2026";

            DiemRenLuyen drl = diemRenLuyenRepository
                    .findBySinhVienIdAndHocKyAndNamHoc(mc.getSinhVien().getId(), hocKy, namHoc)
                    .orElseGet(() -> {
                        DiemRenLuyen newDrl = new DiemRenLuyen();
                        newDrl.setSinhVien(mc.getSinhVien());
                        newDrl.setHocKy(hocKy);
                        newDrl.setNamHoc(namHoc);
                        newDrl.setTongDiem(0);
                        return diemRenLuyenRepository.save(newDrl);
                    });

            drl.setTongDiem(drl.getTongDiem() + qd.getDiemCong());
            diemRenLuyenRepository.save(drl);
        }
        // =============================================

        return minhChungRepository.save(mc);
    }

    @Override
    @Transactional
    public MinhChung reject(Integer id, String lyDo) {
        MinhChung mc = minhChungRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Minh chứng với id=" + id + " không tồn tại"));

        if (!"Chờ duyệt".equals(mc.getTrangThaiDuyet())) {
            throw new BusinessException("Minh chứng đã được xử lý, không thể từ chối nữa");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien approver = sinhVienRepository.findByMssv(username).orElseThrow();

        mc.setTrangThaiDuyet("Bị từ chối");
        mc.setNguoiDuyet(approver);
        mc.setNgayDuyet(LocalDateTime.now());

        String note = mc.getGhiChu() == null ? "" : mc.getGhiChu() + " | ";
        mc.setGhiChu(note + "Lý do từ chối: " + lyDo);

        return minhChungRepository.save(mc);
    }

}
