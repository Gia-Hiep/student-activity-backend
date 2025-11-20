package com.example.studentactivity.controller;

import com.example.studentactivity.entity.Lop;
import com.example.studentactivity.service.LopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "http://localhost:3000")
public class LopController {

    @Autowired
    private LopService lopService;

    @GetMapping
    public ResponseEntity<List<Lop>> getAll(
            @RequestParam(required = false) Integer khoaId
    ) {
        return ResponseEntity.ok(lopService.getAll(khoaId));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Admin','ROLE_Cán bộ khoa')")
    public ResponseEntity<Lop> create(@RequestBody Lop lop) {
        return ResponseEntity.ok(lopService.create(lop));
    }
}
