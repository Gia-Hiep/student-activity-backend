package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.Lop;
import com.example.studentactivity.repository.LopRepository;
import com.example.studentactivity.service.LopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LopServiceImpl implements LopService {

    @Autowired
    private LopRepository lopRepository;

    @Override
    public List<Lop> getAll(Integer khoaId) {
        if (khoaId == null) {
            return lopRepository.findAll();
        }
        return lopRepository.findByKhoaId(khoaId);
    }

    @Override
    public Lop create(Lop lop) {
        return lopRepository.save(lop);
    }
}
