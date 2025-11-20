package com.example.studentactivity.service;

import com.example.studentactivity.entity.KhieuNai;

import java.util.List;

public interface KhieuNaiService {

    KhieuNai create(Integer activityId, String noiDung);

    List<KhieuNai> getMyComplaints();

    List<KhieuNai> getAll(String status);

    KhieuNai handle(Integer id, String trangThai, String phanHoi);
}
