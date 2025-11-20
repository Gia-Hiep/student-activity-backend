package com.example.studentactivity.service;

import com.example.studentactivity.entity.PhanQuyen;

public interface PhanQuyenService {

    PhanQuyen assignRole(
            Integer userId,
            Integer vaiTroId,
            String hocKy,
            String namHoc,
            java.time.LocalDate ngayBatDau,
            java.time.LocalDate ngayKetThuc
    );

    void removeRole(Integer userId, Integer roleId);
}
