package com.example.studentactivity.service;

import com.example.studentactivity.entity.DiemDanh;

public interface DiemDanhService {

    DiemDanh checkIn(Integer hoatDongId);

    DiemDanh checkOut(Integer hoatDongId);
}
