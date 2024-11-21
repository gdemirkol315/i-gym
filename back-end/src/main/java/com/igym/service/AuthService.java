package com.igym.service;

import com.igym.dto.auth.LoginRequest;
import com.igym.dto.auth.LoginResponse;

public interface AuthService {
    LoginResponse authenticate(LoginRequest request);
}
