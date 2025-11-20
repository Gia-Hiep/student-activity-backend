package com.example.studentactivity.controller;

import com.example.studentactivity.entity.TieuChiDrl;
import com.example.studentactivity.entity.QuyDoiDiem;
import com.example.studentactivity.service.DrlConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drl")
@CrossOrigin(origins = "http://localhost:3000")
public class DrlConfigController {

    @Autowired
    private DrlConfigService drlConfigService;

    @GetMapping("/criteria")
    public ResponseEntity<List<TieuChiDrl>> getCriteria() {
        return ResponseEntity.ok(drlConfigService.getAllCriteria());
    }

    @PostMapping("/criteria")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<TieuChiDrl> createCriteria(@RequestBody TieuChiDrl tieuChi) {
        return ResponseEntity.ok(drlConfigService.createCriteria(tieuChi));
    }

    @GetMapping("/rules")
    public ResponseEntity<List<QuyDoiDiem>> getRules() {
        return ResponseEntity.ok(drlConfigService.getAllRules());
    }

    @PostMapping("/rules")
    @PreAuthorize("hasAnyAuthority('ROLE_Cán bộ khoa','ROLE_Admin')")
    public ResponseEntity<QuyDoiDiem> createRule(@RequestBody QuyDoiDiem rule) {
        return ResponseEntity.ok(drlConfigService.createRule(rule));
    }
}
