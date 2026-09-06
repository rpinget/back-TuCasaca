package com.tucasaca.tienda.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.dto.LoginRequestDTO;
import com.tucasaca.tienda.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/api/auth
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    // POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
