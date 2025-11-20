package com.example.studentactivity.controller;

import com.example.studentactivity.entity.DiemDanh;
import com.example.studentactivity.service.DiemDanhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class DiemDanhController {

    @Autowired
    private DiemDanhService diemDanhService;

    public record AttendanceRequest(Integer hoatDongId) {}

    @PostMapping("/check-in")
    public ResponseEntity<DiemDanh> checkIn(@RequestBody AttendanceRequest req) {
        DiemDanh d = diemDanhService.checkIn(req.hoatDongId());
        return ResponseEntity.ok(d);
    }

    @PostMapping("/check-out")
    public ResponseEntity<DiemDanh> checkOut(@RequestBody AttendanceRequest req) {
        DiemDanh d = diemDanhService.checkOut(req.hoatDongId());
        return ResponseEntity.ok(d);
    }
}
