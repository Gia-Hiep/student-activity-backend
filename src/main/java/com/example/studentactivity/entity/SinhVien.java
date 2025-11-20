package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sinh_vien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mssv", unique = true, nullable = false)
    private String mssv;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    private String email;

    @Column(name = "mat_khau")
    private String matKhau; // Should be hashed in service layer

    @ManyToOne
    @JoinColumn(name = "id_lop")
    private Lop lop;

    @Column(name = "trang_thai")
    private String trangThai = "Hoạt động";
}