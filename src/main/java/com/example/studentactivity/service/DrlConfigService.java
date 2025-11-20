package com.example.studentactivity.service;

import com.example.studentactivity.entity.TieuChiDrl;
import com.example.studentactivity.entity.QuyDoiDiem;

import java.util.List;

public interface DrlConfigService {

    List<TieuChiDrl> getAllCriteria();

    TieuChiDrl createCriteria(TieuChiDrl tieuChiDrl);

    List<QuyDoiDiem> getAllRules();

    QuyDoiDiem createRule(QuyDoiDiem rule);
}
