package com.example.studentactivity.repository;

import com.example.studentactivity.entity.TieuChiDrl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TieuChiDrlRepository extends JpaRepository<TieuChiDrl, Integer> {
    List<TieuChiDrl> findByNhom(String nhom);
}