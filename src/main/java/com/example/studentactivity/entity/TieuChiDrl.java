package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tieu_chi_drl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TieuChiDrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_tc")
    private String maTc;

    @Column(name = "ten_tieu_chi")
    private String tenTieuChi;

    @Column(name = "diem_toi_da")
    private Integer diemToiDa;

    @Column(name = "nhom")
    private String nhom;
}