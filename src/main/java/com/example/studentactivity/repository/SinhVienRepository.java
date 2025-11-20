package com.example.studentactivity.repository;

import com.example.studentactivity.entity.PhanQuyen;
import com.example.studentactivity.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface SinhVienRepository extends JpaRepository<SinhVien, Integer> {
    Optional<SinhVien> findByMssv(String mssv);
    List<SinhVien> findByLopId(Integer lopId);

    @Query("SELECT sv FROM SinhVien sv WHERE sv.lop.khoa.id = :khoaId")
    List<SinhVien> findByKhoaId(Integer khoaId);

    @Query("SELECT pq FROM PhanQuyen pq " +
            "WHERE pq.sinhVien.id = :sinhVienId " +
            "AND pq.ngayBatDau <= :today " +
            "AND pq.ngayKetThuc >= :today")
    List<PhanQuyen> findPhanQuyenBySinhVienIdAndActive(@Param("sinhVienId") Integer sinhVienId,
                                                       @Param("today") LocalDate today);
}