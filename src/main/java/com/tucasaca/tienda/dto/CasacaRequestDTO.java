package com.tucasaca.tienda.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasacaRequestDTO {
    private Long equipoId;
    private Long ligaId;
    private Integer anio;
    private String jugador;
    private Integer numero;
    private String talle;
    private BigDecimal precio;
    private String imagenUrl;
    private Boolean activo;
}
