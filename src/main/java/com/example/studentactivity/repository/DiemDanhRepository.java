package com.example.studentactivity.repository;

import com.example.studentactivity.entity.DiemDanh;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DiemDanhRepository extends JpaRepository<DiemDanh, Integer> {

    Optional<DiemDanh> findBySinhVienIdAndHoatDongId(Integer sinhVienId, Integer hoatDongId);

    Long countByHoatDongIdAndCheckInIsNotNull(Integer hoatDongId);
}