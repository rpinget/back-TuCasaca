package com.tucasaca.tienda.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.dto.LoginRequestDTO;
import com.tucasaca.tienda.dto.LoginResponseDTO;
import com.tucasaca.tienda.security.AuthCookieService;
import com.tucasaca.tienda.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/api/auth
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthCookieService authCookieService;

    // POST http://localhost:8080/api/auth/login
    // El JWT viaja en una cookie HttpOnly (Set-Cookie), nunca en el cuerpo de la respuesta.
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO login = authenticationService.authenticate(request);

        // Copia sin token: el front recibe solo los datos del usuario
        LoginResponseDTO body = new LoginResponseDTO(
                null, login.getId(), login.getEmail(), login.getNombre(), login.getRol());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookieService.crear(login.getToken()).toString())
                .body(body);
    }

    // POST http://localhost:8080/api/auth/logout
    // JS no puede borrar una cookie HttpOnly, por eso el backend la expira.
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, authCookieService.borrar().toString())
                .build();
    }
}
