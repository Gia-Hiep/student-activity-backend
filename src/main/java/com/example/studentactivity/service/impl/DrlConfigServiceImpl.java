package com.example.studentactivity.service.impl;

import com.example.studentactivity.entity.TieuChiDrl;
import com.example.studentactivity.entity.QuyDoiDiem;
import com.example.studentactivity.repository.TieuChiDrlRepository;
import com.example.studentactivity.repository.QuyDoiDiemRepository;
import com.example.studentactivity.service.DrlConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrlConfigServiceImpl implements DrlConfigService {

    @Autowired
    private TieuChiDrlRepository tieuChiDrlRepository;

    @Autowired
    private QuyDoiDiemRepository quyDoiDiemRepository;

    @Override
    public List<TieuChiDrl> getAllCriteria() {
        return tieuChiDrlRepository.findAll();
    }

    @Override
    public TieuChiDrl createCriteria(TieuChiDrl tieuChiDrl) {
        return tieuChiDrlRepository.save(tieuChiDrl);
    }

    @Override
    public List<QuyDoiDiem> getAllRules() {
        return quyDoiDiemRepository.findAll();
    }

    @Override
    public QuyDoiDiem createRule(QuyDoiDiem rule) {
        return quyDoiDiemRepository.save(rule);
    }
}
