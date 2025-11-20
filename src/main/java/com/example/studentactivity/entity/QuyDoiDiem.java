package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quy_doi_diem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuyDoiDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_loai_hd")
    private LoaiHoatDong loaiHoatDong;

    @ManyToOne
    @JoinColumn(name = "id_tieu_chi")
    private TieuChiDrl tieuChi;

    @Column(name = "diem_cong")
    private Integer diemCong;

    @Column(name = "gioi_han_moi_hk")
    private Integer gioiHanMoiHk;
}