package com.tucasaca.tienda.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tucasaca.tienda.dto.UsuarioRegistroDTO;
import com.tucasaca.tienda.dto.UsuarioResponseDTO;
import com.tucasaca.tienda.mapper.UsuarioMapper;
import com.tucasaca.tienda.model.Usuario;
import com.tucasaca.tienda.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO registroDTO) {
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(registroDTO);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }
}