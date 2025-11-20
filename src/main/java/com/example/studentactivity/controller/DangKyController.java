package com.example.studentactivity.controller;

import com.example.studentactivity.entity.DangKy;
import com.example.studentactivity.repository.DangKyRepository;
import com.example.studentactivity.service.DangKyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "http://localhost:3000")
public class DangKyController {

    @Autowired
    private DangKyService dangKyService;
    @Autowired
    private DangKyRepository dangKyRepository;

    record DangKyRequest(Integer hoatDongId) {}

    @PostMapping
    public ResponseEntity<DangKy> register(@RequestBody DangKyRequest request) {
        DangKy dk = dangKyService.register(request.hoatDongId());
        return ResponseEntity.ok(dk);
    }

    @GetMapping("/my")
    public ResponseEntity<List<DangKy>> getMyRegistrations() {
        return ResponseEntity.ok(dangKyService.getMyRegistrations());
    }

    @GetMapping("/activity/{hoatDongId}")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán sự lớp','ROLE_Liên chi hội','ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<List<DangKy>> getByActivity(@PathVariable Integer hoatDongId) {
        return ResponseEntity.ok(dangKyService.getByHoatDongId(hoatDongId));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count(@RequestParam Integer hoatDongId) {
        long count = dangKyRepository.countByHoatDongId(hoatDongId);
        return ResponseEntity.ok(count);
    }

}