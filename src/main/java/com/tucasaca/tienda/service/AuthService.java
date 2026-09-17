package com.tucasaca.tienda.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tucasaca.tienda.dto.AuthResponseDTO;
import com.tucasaca.tienda.dto.LoginRequestDTO;
import com.tucasaca.tienda.dto.UsuarioRegistroDTO;
import com.tucasaca.tienda.exception.EmailDuplicadoException;
import com.tucasaca.tienda.model.Role;
import com.tucasaca.tienda.model.Usuario;
import com.tucasaca.tienda.repository.UsuarioRepository;
import com.tucasaca.tienda.security.JwtUtil;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDTO register(UsuarioRegistroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailDuplicadoException(dto.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fechaNacimiento(dto.getFechaNacimiento())
                .sexo(dto.getSexo())
                .role(Role.USER)
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(usuario.getEmail(), List.of("ROLE_USER"));
        return new AuthResponseDTO(token, usuario.getEmail(), "USER");
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow();
        List<String> roles = List.of("ROLE_" + usuario.getRole().name());
        String token = jwtUtil.generateToken(usuario.getEmail(), roles);

        return new AuthResponseDTO(token, usuario.getEmail(), usuario.getRole().name());
    }
}
