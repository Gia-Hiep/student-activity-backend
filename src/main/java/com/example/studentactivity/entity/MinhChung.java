package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "minh_chung")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinhChung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_sinh_vien")
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "id_hd")
    private HoatDong hoatDong;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "trang_thai_duyet")
    private String trangThaiDuyet = "Chờ duyệt";

    @ManyToOne
    @JoinColumn(name = "nguoi_duyet")
    private SinhVien nguoiDuyet;

    @Column(name = "ngay_duyet")
    private LocalDateTime ngayDuyet;
}