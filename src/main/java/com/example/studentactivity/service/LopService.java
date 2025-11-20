package com.example.studentactivity.service;

import com.example.studentactivity.entity.Lop;

import java.util.List;

public interface LopService {

    List<Lop> getAll(Integer khoaId);

    Lop create(Lop lop);
}
