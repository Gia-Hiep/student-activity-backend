package com.example.studentactivity.controller;

import com.example.studentactivity.entity.LoaiHoatDong;
import com.example.studentactivity.service.LoaiHoatDongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/activity-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LoaiHoatDongController {

    private final LoaiHoatDongService loaiHoatDongService;

    @GetMapping
    public List<LoaiHoatDong> getAll() {
        return loaiHoatDongService.getAll();
    }
}
