package com.example.studentactivity.service;

import com.example.studentactivity.entity.DiemRenLuyen;

public interface DiemRenLuyenService {
    DiemRenLuyen getMyScore(String hocKy, String namHoc);
    byte[] exportExcel(Integer khoaId, String hocKy, String namHoc);
}