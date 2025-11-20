package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "khieu_nai")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhieuNai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_sinh_vien")
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "id_hd")
    private HoatDong hoatDong;

    @Column(name = "hoc_ky")
    private String hocKy;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "trang_thai")
    private String trangThai = "Chờ xử lý";

    @ManyToOne
    @JoinColumn(name = "nguoi_xu_ly")
    private SinhVien nguoiXuLy;

    @Column(name = "phan_hoi", columnDefinition = "TEXT")
    private String phanHoi;

}