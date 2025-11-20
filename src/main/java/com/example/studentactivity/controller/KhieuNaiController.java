package com.example.studentactivity.controller;

import com.example.studentactivity.entity.KhieuNai;
import com.example.studentactivity.service.KhieuNaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "http://localhost:3000")
public class KhieuNaiController {

    @Autowired
    private KhieuNaiService khieuNaiService;

    // Request khi tạo khiếu nại
    public record CreateComplaintRequest(
            Integer activityId,
            String noiDung
    ) {}

    // Request xử lý khiếu nại
    public record HandleComplaintRequest(
            String trangThai,
            String phanHoi
    ) {}

    // 🔥 Sinh viên gửi khiếu nại
    @PostMapping
    public ResponseEntity<KhieuNai> create(@RequestBody CreateComplaintRequest req) {

        if (req.activityId() == null) {
            return ResponseEntity.badRequest().build();
        }

        KhieuNai k = khieuNaiService.create(req.activityId(), req.noiDung());
        return ResponseEntity.ok(k);
    }

    // Sinh viên xem khiếu nại của mình
    @GetMapping("/my")
    public ResponseEntity<List<KhieuNai>> getMyComplaints() {
        return ResponseEntity.ok(khieuNaiService.getMyComplaints());
    }

    // Cán bộ khoa xem tất cả
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<List<KhieuNai>> getAll(
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(khieuNaiService.getAll(status));
    }

    // Cán bộ khoa xử lý khiếu nại
    @PutMapping("/{id}/handle")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<KhieuNai> handle(
            @PathVariable Integer id,
            @RequestBody HandleComplaintRequest req
    ) {
        return ResponseEntity.ok(
                khieuNaiService.handle(id, req.trangThai(), req.phanHoi())
        );
    }
}

