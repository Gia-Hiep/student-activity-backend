package com.example.studentactivity.repository;

import com.example.studentactivity.entity.Lop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LopRepository extends JpaRepository<Lop, Integer> {
    List<Lop> findByKhoaId(Integer khoaId);
}