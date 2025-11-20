package com.example.studentactivity.controller;

import com.example.studentactivity.entity.Lop;
import com.example.studentactivity.entity.SinhVien;
import com.example.studentactivity.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = "http://localhost:3000")
public class DebugRegisterController {

    @Autowired private SinhVienRepository sinhVienRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/create-test-user")
    public String createTestUser(
            @RequestParam String mssv,
            @RequestParam String hoTen,
            @RequestParam String matKhau,
            @RequestParam(defaultValue = "1") Integer idLop) {

        if (sinhVienRepository.findByMssv(mssv).isPresent()) {
            return "User " + mssv + " đã tồn tại!";
        }

        SinhVien sv = new SinhVien();
        sv.setMssv(mssv);
        sv.setHoTen(hoTen);
        sv.setEmail(mssv + "@hutech.edu.vn");
        sv.setMatKhau(passwordEncoder.encode(matKhau));

        // Hỗ trợ cả 2 kiểu entity phổ biến
        Lop lop = new Lop();
        lop.setId(idLop);
        sv.setLop(lop);  // nếu entity có field Lop lop
        // sv.setIdLop(idLop); // nếu có field Integer idLop thì dùng cái này

        sv.setTrangThai("Hoạt động");
        sinhVienRepository.save(sv);

        return "TẠO THÀNH CÔNG: " + mssv + " | Mật khẩu: " + matKhau + " | Hash đúng 100%!";
    }
}