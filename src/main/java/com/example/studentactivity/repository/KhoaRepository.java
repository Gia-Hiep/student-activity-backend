package com.example.studentactivity.repository;

import com.example.studentactivity.entity.Khoa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KhoaRepository extends JpaRepository<Khoa, Integer> {
    List<Khoa> findByTruongId(Integer truongId);
}