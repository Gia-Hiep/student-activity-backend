package com.example.studentactivity.controller;

import com.example.studentactivity.entity.Khoa;
import com.example.studentactivity.service.KhoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculties")
@CrossOrigin(origins = "http://localhost:3000")
public class KhoaController {

    @Autowired
    private KhoaService khoaService;

    @GetMapping
    public ResponseEntity<List<Khoa>> getAll() {
        return ResponseEntity.ok(khoaService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Admin')")
    public ResponseEntity<Khoa> create(@RequestBody Khoa khoa) {
        return ResponseEntity.ok(khoaService.create(khoa));
    }
}
