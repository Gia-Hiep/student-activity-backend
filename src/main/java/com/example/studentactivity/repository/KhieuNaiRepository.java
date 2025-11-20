package com.example.studentactivity.repository;

import com.example.studentactivity.entity.KhieuNai;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KhieuNaiRepository extends JpaRepository<KhieuNai, Integer> {

    List<KhieuNai> findBySinhVienId(Integer sinhVienId);

    List<KhieuNai> findByTrangThai(String trangThai);
}