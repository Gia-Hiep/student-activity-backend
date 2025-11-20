package com.example.studentactivity.repository;

import com.example.studentactivity.entity.HoatDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface HoatDongRepository extends JpaRepository<HoatDong, Integer> {

    List<HoatDong> findByKhoaId(Integer khoaId);

    List<HoatDong> findByTrangThai(String trangThai);

    List<HoatDong> findByKhoaIdAndTrangThai(Integer khoaId, String trangThai);

    @Query("SELECT hd FROM HoatDong hd WHERE hd.thoiGianBd BETWEEN :from AND :to")
    List<HoatDong> findByThoiGianBdBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT COUNT(dk) FROM DangKy dk WHERE dk.hoatDong.id = :hoatDongId")
    Long countRegistrationsByHoatDongId(Integer hoatDongId);

    List<HoatDong> findByThoiGianBdAfterAndTrangThaiOrderByThoiGianBdAsc(LocalDateTime now, String mở);
    List<HoatDong> findByCapToChucAndKhoaId(String cap, Integer khoaId);

    List<HoatDong> findByCapToChucAndLopId(String cap, Integer lopId);

    boolean existsByParentIdAndLopId(Integer parentId, Integer lopId);

    List<HoatDong> findByThoiGianKtAfterOrderByThoiGianBdAsc(LocalDateTime now);

    List<HoatDong> findByCapToChuc(String khoa);
}