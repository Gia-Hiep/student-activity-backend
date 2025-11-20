package com.example.studentactivity.service;

import com.example.studentactivity.entity.Khoa;

import java.util.List;

public interface KhoaService {

    List<Khoa> getAll();

    Khoa create(Khoa khoa);
}
