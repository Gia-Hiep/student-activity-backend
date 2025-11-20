package com.example.studentactivity.service;

import com.example.studentactivity.entity.PhanQuyen;
import java.util.List;

public interface UserRoleService {
    List<PhanQuyen> getActiveRoles(Integer sinhVienId);
    boolean hasAuthority(String authority);
    Integer getCurrentSinhVienId();
}