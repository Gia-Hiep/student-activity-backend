package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "dang_ky")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DangKy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_sinh_vien")
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "id_hd")
    private HoatDong hoatDong;

    @Column(name = "thoi_diem_dk")
    private LocalDateTime thoiDiemDk = LocalDateTime.now();

    @Column(name = "trang_thai_dk")
    private String trangThaiDk = "Đăng ký";

    @Column(name = "da_check_in")
    private Boolean daCheckIn = false;

    @Column(name = "da_check_out")
    private Boolean daCheckOut = false;

}