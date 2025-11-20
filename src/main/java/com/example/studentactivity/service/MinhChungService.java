package com.example.studentactivity.service;

import com.example.studentactivity.entity.MinhChung;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MinhChungService {
    MinhChung upload(Integer hoatDongId, MultipartFile file, String ghiChu);
    List<MinhChung> getMyEvidences();
    List<MinhChung> getPendingForApproval();
    MinhChung approve(Integer id);
    MinhChung reject(Integer id, String lyDo);
}