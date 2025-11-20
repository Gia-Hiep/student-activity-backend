package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.Khoa;
import com.example.studentactivity.repository.KhoaRepository;
import com.example.studentactivity.service.KhoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhoaServiceImpl implements KhoaService {

    @Autowired
    private KhoaRepository khoaRepository;

    @Override
    public List<Khoa> getAll() {
        return khoaRepository.findAll();
    }

    @Override
    public Khoa create(Khoa khoa) {
        return khoaRepository.save(khoa);
    }
}
