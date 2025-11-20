package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diem_ren_luyen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiemRenLuyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_sinh_vien")
    private SinhVien sinhVien;

    @Column(name = "hoc_ky")
    private String hocKy;

    @Column(name = "nam_hoc")
    private String namHoc;

    @Column(name = "tong_diem")
    private Integer tongDiem = 0;

    @Column(name = "xep_loai")
    private String xepLoai;

    @Column(name = "ghi_chu")
    private String ghiChu;
}