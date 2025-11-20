package com.example.studentactivity.service;

import com.example.studentactivity.entity.HoatDong;
import java.util.List;

public interface HoatDongService {
    List<HoatDong> getAll(Integer khoaId);
    HoatDong getById(Integer id);
    HoatDong create(HoatDong hoatDong);
    HoatDong update(Integer id, HoatDong hoatDong);
    void delete(Integer id);
    boolean isFull(Integer hoatDongId);

    HoatDong createFacultyActivity(HoatDong hd);

    HoatDong deployFacultyActivityToClass(Integer id);
}