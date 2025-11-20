package com.example.studentactivity.controller;

import com.example.studentactivity.entity.HoatDong;
import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.repository.SinhVienRepository;
import com.example.studentactivity.exception.BusinessException;
import com.example.studentactivity.service.HoatDongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.studentactivity.repository.HoatDongRepository;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class HoatDongController {

    private final HoatDongService hoatDongService;
    private final HoatDongRepository hoatDongRepository;
    private final SinhVienRepository sinhVienRepository;
    @GetMapping
    public ResponseEntity<List<HoatDong>> getAll(@RequestParam(required = false) Integer khoaId) {
        List<HoatDong> list = hoatDongService.getAll(khoaId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoatDong> getById(@PathVariable Integer id) {
        HoatDong hd = hoatDongService.getById(id);
        return ResponseEntity.ok(hd);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Cán sự lớp','ROLE_Liên chi hội','ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<HoatDong> create(@RequestBody HoatDong hoatDong) {
        HoatDong created = hoatDongService.create(hoatDong);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán sự lớp','ROLE_Liên chi hội','ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<HoatDong> update(@PathVariable Integer id, @RequestBody HoatDong hoatDong) {
        HoatDong updated = hoatDongService.update(id, hoatDong);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        hoatDongService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/upcoming")
    public List<HoatDong> getUpcoming() {
        LocalDateTime now = LocalDateTime.now();

        return hoatDongRepository.findByThoiGianKtAfterOrderByThoiGianBdAsc(now);
    }

    @GetMapping("/faculty")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin,ROLE_Cán sự lớp','ROLE_Liên chi hội')")
    public List<HoatDong> getFacultyActivities() {
        return hoatDongRepository.findByCapToChuc("KHOA");
    }

    @GetMapping("/class")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán sự lớp','ROLE_Liên chi hội')")
    public List<HoatDong> getClassActivities() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SinhVien sv = sinhVienRepository.findByMssv(username)
                .orElseThrow(() -> new BusinessException("Không tìm thấy sinh viên"));

        return hoatDongRepository.findByCapToChucAndLopId("LOP", sv.getLop().getId());
    }

    @PostMapping("/faculty")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<HoatDong> createFacultyActivity(@RequestBody HoatDong hd) {
        return ResponseEntity.ok(hoatDongService.createFacultyActivity(hd));
    }

    @PostMapping("/deploy-to-class/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán sự lớp','ROLE_Liên chi hội')")
    public HoatDong deployToClass(@PathVariable Integer id) {
        return hoatDongService.deployFacultyActivityToClass(id);
    }
}