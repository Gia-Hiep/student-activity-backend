package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.LoaiHoatDong;
import com.example.studentactivity.repository.LoaiHoatDongRepository;
import com.example.studentactivity.service.LoaiHoatDongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoaiHoatDongServiceImpl implements LoaiHoatDongService {

    private final LoaiHoatDongRepository repo;

    @Override
    public List<LoaiHoatDong> getAll() {
        return repo.findAll();
    }
}
