package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.DiemRenLuyen;
import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.repository.DiemRenLuyenRepository;
import com.example.studentactivity.repository.SinhVienRepository;
import com.example.studentactivity.service.DiemRenLuyenService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class DiemRenLuyenServiceImpl implements DiemRenLuyenService {

    @Autowired private DiemRenLuyenRepository diemRenLuyenRepository;
    @Autowired private SinhVienRepository sinhVienRepository;

    @Override
    public DiemRenLuyen getMyScore(String hocKy, String namHoc) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien sv = sinhVienRepository.findByMssv(username).orElseThrow();
        return diemRenLuyenRepository.findBySinhVienIdAndHocKyAndNamHoc(sv.getId(), hocKy, namHoc)
                .orElse(new DiemRenLuyen());
    }

    @Override
    public byte[] exportExcel(Integer khoaId, String hocKy, String namHoc) {
        List<SinhVien> listSv = sinhVienRepository.findByKhoaId(khoaId);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Điểm rèn luyện");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("MSSV");
        header.createCell(1).setCellValue("Họ tên");
        header.createCell(2).setCellValue("Tổng điểm");
        header.createCell(3).setCellValue("Xếp loại");

        int rowNum = 1;
        for (SinhVien sv : listSv) {
            DiemRenLuyen drl = diemRenLuyenRepository
                    .findBySinhVienIdAndHocKyAndNamHoc(sv.getId(), hocKy, namHoc)
                    .orElse(new DiemRenLuyen());
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sv.getMssv());
            row.createCell(1).setCellValue(sv.getHoTen());
            row.createCell(2).setCellValue(drl.getTongDiem() != null ? drl.getTongDiem() : 0);
            row.createCell(3).setCellValue(drl.getXepLoai() != null ? drl.getXepLoai() : "");
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            workbook.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xuất Excel");
        }
    }
}