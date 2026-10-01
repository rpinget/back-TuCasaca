package com.tucasaca.tienda.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.tucasaca.tienda.dto.LoginRequestDTO;
import com.tucasaca.tienda.model.Usuario;
import com.tucasaca.tienda.repository.UsuarioRepository;
import com.tucasaca.tienda.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public String authenticate(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        Set<String> roles = usuario.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toSet());

        return jwtUtil.generateToken(usuario.getEmail(), roles);
    }
}
