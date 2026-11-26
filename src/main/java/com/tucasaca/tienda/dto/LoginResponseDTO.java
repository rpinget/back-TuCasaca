package com.tucasaca.tienda.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL) // el token no se expone en el JSON del login
    private String token;
    private Long id;
    private String email;
    private String nombre;
    private String rol;
}
