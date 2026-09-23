package com.medicare.auth.controller;

import com.medicare.auth.model.AuthRequest;
import com.medicare.auth.model.AuthResponse;
import com.medicare.auth.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

/**
 * Demo authentication endpoint for MediCare Health Alliances.
 * For the purpose of this SOA project, any username/password combination
 * is accepted and a signed JWT is issued (patients/doctors/admin).
 * Replace with a real user store / password check for production use.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        String role = (request.getRole() == null || request.getRole().isBlank())
                ? "PATIENT" : request.getRole().toUpperCase();
        String token = jwtUtil.generateToken(request.getUsername(), role);
        return new AuthResponse(token);
    }

    @GetMapping("/health")
    public String health() {
        return "Auth Service is UP";
    }
}
