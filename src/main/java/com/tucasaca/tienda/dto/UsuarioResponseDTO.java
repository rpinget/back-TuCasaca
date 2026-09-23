package com.tucasaca.tienda.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tucasaca.tienda.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaNacimiento;
    private String sexo;
    private Role rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}