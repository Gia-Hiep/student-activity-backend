package com.example.studentactivity.repository;

import com.example.studentactivity.entity.PhanQuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface PhanQuyenRepository extends JpaRepository<PhanQuyen, Integer> {

    List<PhanQuyen> findBySinhVienId(Integer sinhVienId);
    void deleteBySinhVienIdAndVaiTroId(Integer sinhVienId, Integer vaiTroId);

    @Query("SELECT pq FROM PhanQuyen pq WHERE pq.sinhVien.id = :sinhVienId " +
            "AND pq.ngayBatDau <= :today AND pq.ngayKetThuc >= :today")
    List<PhanQuyen> findActiveRolesBySinhVienId(Integer sinhVienId, LocalDate today);

    List<PhanQuyen> findBySinhVienIdAndHocKyAndNamHoc(Integer sinhVienId, String hocKy, String namHoc);
}