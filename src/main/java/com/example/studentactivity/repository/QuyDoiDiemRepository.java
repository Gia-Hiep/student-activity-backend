package com.example.studentactivity.repository;

import com.example.studentactivity.entity.QuyDoiDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QuyDoiDiemRepository extends JpaRepository<QuyDoiDiem, Integer> {

    Optional<QuyDoiDiem> findByLoaiHoatDongIdAndTieuChiId(Integer loaiHdId, Integer tieuChiId);
}