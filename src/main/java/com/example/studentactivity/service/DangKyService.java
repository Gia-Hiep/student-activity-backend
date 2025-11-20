package com.example.studentactivity.service;

import com.example.studentactivity.entity.DangKy;
import com.example.studentactivity.entity.HoatDong;

import java.util.List;

public interface DangKyService {
    DangKy register(Integer hoatDongId);
    List<DangKy> getMyRegistrations();
    List<DangKy> getByHoatDongId(Integer hoatDongId);
}