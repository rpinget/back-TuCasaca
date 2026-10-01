package com.tucasaca.tienda.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasacaRequestDTO {
    @NotNull(message = "El equipoId es obligatorio")
    private Long equipoId;

    @NotNull(message = "El ligaId es obligatorio")
    private Long ligaId;

    private Integer anio;
    private String jugador;
    private Integer numero;

    @NotBlank(message = "El talle es obligatorio")
    private String talle;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock = 0;

    private String descripcion;
    private String imagenUrl;
    private Boolean activo;
}
