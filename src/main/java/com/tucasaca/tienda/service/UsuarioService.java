package com.tucasaca.tienda.service;

import java.util.List;

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

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO registroDTO) {
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(registroDTO);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }
}