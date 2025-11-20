package com.example.studentactivity.repository;

import com.example.studentactivity.entity.DiemRenLuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DiemRenLuyenRepository extends JpaRepository<DiemRenLuyen, Integer> {

    Optional<DiemRenLuyen> findBySinhVienIdAndHocKyAndNamHoc(Integer sinhVienId, String hocKy, String namHoc);
}