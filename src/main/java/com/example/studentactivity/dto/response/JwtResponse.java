package com.example.studentactivity.dto.response;

import java.util.List;

public record JwtResponse(String token, String mssv, List<String> roles) {}