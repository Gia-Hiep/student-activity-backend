package com.example.studentactivity.controller;

import com.example.studentactivity.entity.MinhChung;
import com.example.studentactivity.service.MinhChungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/evidences")
@CrossOrigin(origins = "http://localhost:3000")
public class MinhChungController {

    @Autowired
    private MinhChungService minhChungService;

    @PostMapping("/upload")
    public ResponseEntity<MinhChung> upload(
            @RequestParam("hoatDongId") Integer hoatDongId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "ghiChu", required = false) String ghiChu) {
        MinhChung mc = minhChungService.upload(hoatDongId, file, ghiChu);
        return ResponseEntity.ok(mc);
    }

    @GetMapping("/my")
    public ResponseEntity<List<MinhChung>> getMyEvidences() {
        return ResponseEntity.ok(minhChungService.getMyEvidences());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán sự lớp','ROLE_Liên chi hội','ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<List<MinhChung>> getPending() {
        return ResponseEntity.ok(minhChungService.getPendingForApproval());
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán sự lớp','ROLE_Liên chi hội','ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<MinhChung> approve(@PathVariable Integer id) {
        return ResponseEntity.ok(minhChungService.approve(id));
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán sự lớp','ROLE_Liên chi hội','ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<MinhChung> reject(@PathVariable Integer id, @RequestBody String lyDo) {
        return ResponseEntity.ok(minhChungService.reject(id, lyDo));
    }
}