package com.tucasaca.tienda.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasacaDTO {
    private Long id;
    private EquipoDTO equipo;
    private LigaDTO liga;
    private Integer anio;
    private String jugador;
    private Integer numero;
    private String talle;
    private BigDecimal precio;
    private String imagenUrl;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
