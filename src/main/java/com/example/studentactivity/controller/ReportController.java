package com.example.studentactivity.controller;

import com.example.studentactivity.entity.DiemRenLuyen;
import com.example.studentactivity.service.DiemRenLuyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:3000")
public class ReportController {

    @Autowired
    private DiemRenLuyenService diemRenLuyenService;

    @GetMapping("/my-score")
    public ResponseEntity<DiemRenLuyen> getMyScore(
            @RequestParam String hocKy,
            @RequestParam String namHoc) {
        return ResponseEntity.ok(diemRenLuyenService.getMyScore(hocKy, namHoc));
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam Integer khoaId,
            @RequestParam String hocKy,
            @RequestParam String namHoc) {
        byte[] excel = diemRenLuyenService.exportExcel(khoaId, hocKy, namHoc);
        ByteArrayResource resource = new ByteArrayResource(excel);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=diem_ren_luyen.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}