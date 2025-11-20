package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loai_hoat_dong")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiHoatDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_loai", nullable = false)
    private String tenLoai;

    @Column(name = "diem_quy_doi")
    private Integer diemQuyDoi = 0;
}