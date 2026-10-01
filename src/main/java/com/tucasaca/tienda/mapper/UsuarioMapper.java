package com.tucasaca.tienda.mapper;

import org.springframework.stereotype.Component;

import com.tucasaca.tienda.dto.UsuarioRegistroDTO;
import com.tucasaca.tienda.dto.UsuarioResponseDTO;
import com.tucasaca.tienda.model.Usuario;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRegistroDTO dto) {
        if (dto == null) {
            return null;
        }

        return Usuario.builder()
                .nombreUsuario(dto.getNombreUsuario())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .fechaNacimiento(dto.getFechaNacimiento())
                .sexo(dto.getSexo())
                .build();
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getFechaNacimiento(),
                usuario.getSexo(),
                usuario.getRol(),
                usuario.getActivo(),
                usuario.getFechaCreacion());
    }
}