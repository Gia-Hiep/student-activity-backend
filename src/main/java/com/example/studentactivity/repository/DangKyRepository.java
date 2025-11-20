package com.example.studentactivity.repository;

import com.example.studentactivity.entity.DangKy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DangKyRepository extends JpaRepository<DangKy, Integer> {

    List<DangKy> findBySinhVienId(Integer sinhVienId);

    List<DangKy> findByHoatDongId(Integer hoatDongId);

    Optional<DangKy> findBySinhVienIdAndHoatDongId(Integer sinhVienId, Integer hoatDongId);

    Long countByHoatDongId(Integer hoatDongId);
}