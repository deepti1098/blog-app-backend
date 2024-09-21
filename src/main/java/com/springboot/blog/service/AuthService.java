package com.springboot.blog.service;

import com.springboot.blog.payload.LoginDTO;
import com.springboot.blog.payload.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
    String register(RegisterDTO registerDTO);
}
