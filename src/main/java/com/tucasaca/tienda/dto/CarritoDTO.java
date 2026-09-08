package com.tucasaca.tienda.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private String estado;
    private List<ItemCarritoDTO> items;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
}
