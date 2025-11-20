package com.example.studentactivity.repository;

import com.example.studentactivity.entity.MinhChung;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MinhChungRepository extends JpaRepository<MinhChung, Integer> {

    List<MinhChung> findBySinhVienId(Integer sinhVienId);

    List<MinhChung> findByHoatDongId(Integer hoatDongId);

    List<MinhChung> findByTrangThaiDuyet(String trangThaiDuyet);

    List<MinhChung> findByHoatDong_Khoa_Id(Integer khoaId);
}