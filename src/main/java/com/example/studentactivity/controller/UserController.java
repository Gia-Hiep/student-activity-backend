package com.example.studentactivity.controller;

import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.entity.PhanQuyen;
import com.example.studentactivity.service.SinhVienService;
import com.example.studentactivity.service.PhanQuyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private PhanQuyenService phanQuyenService;

    // DTO đơn giản cho gán role
    public record AssignRoleRequest(
            Integer vaiTroId,
            String hocKy,
            String namHoc,
            LocalDate ngayBatDau,
            LocalDate ngayKetThuc
    ) {}

    @GetMapping("/me")
    public ResponseEntity<SinhVien> getCurrentUser() {
        return ResponseEntity.ok(sinhVienService.getCurrentUser());
    }

    @GetMapping("/by-class/{lopId}")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<List<SinhVien>> getByClass(@PathVariable Integer lopId) {
        return ResponseEntity.ok(sinhVienService.getByLopId(lopId));
    }

    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<PhanQuyen> assignRole(
            @PathVariable Integer userId,
            @RequestBody AssignRoleRequest req
    ) {
        PhanQuyen pq = phanQuyenService.assignRole(
                userId,
                req.vaiTroId(),
                req.hocKy(),
                req.namHoc(),
                req.ngayBatDau(),
                req.ngayKetThuc()
        );
        return ResponseEntity.ok(pq);
    }   

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<?> removeRole(
            @PathVariable Integer userId,
            @PathVariable Integer roleId
    ) {
        phanQuyenService.removeRole(userId, roleId);
        return ResponseEntity.ok().build();
    }
}
