package com.example.studentactivity.service;

import com.example.studentactivity.entity.SinhVien;

import java.util.List;

public interface SinhVienService {

    SinhVien getCurrentUser();

    List<SinhVien> getByLopId(Integer lopId);
}
