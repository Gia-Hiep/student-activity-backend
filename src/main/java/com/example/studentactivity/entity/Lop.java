package com.example.studentactivity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lop")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_lop", nullable = false)
    private String tenLop;

    @ManyToOne
    @JoinColumn(name = "id_khoa")
    private Khoa khoa;

    @Column(name = "co_van")
    private String coVan;
}