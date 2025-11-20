package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hoat_dong")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoatDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_hd", nullable = false)
    private String tenHd;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "id_loai")
    private LoaiHoatDong loai;

    @ManyToOne
    @JoinColumn(name = "id_khoa")
    private Khoa khoa;

    @Column(name = "thoi_gian_bd")
    private LocalDateTime thoiGianBd;

    @Column(name = "thoi_gian_kt")
    private LocalDateTime thoiGianKt;

    @Column(name = "dia_diem")
    private String diaDiem;

    @Column(name = "gioi_han_sv")
    private Integer gioiHanSv ;

    @ManyToOne
    @JoinColumn(name = "nguoi_tao")
    private SinhVien nguoiTao;

    @Column(name = "trang_thai")
    private String trangThai = "Mở";

    private String capToChuc; // "KHOA" hoặc "LOP"

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private HoatDong parent; // null nếu là hoạt động gốc

    @ManyToOne
    @JoinColumn(name = "lop_id")
    private Lop lop;

}