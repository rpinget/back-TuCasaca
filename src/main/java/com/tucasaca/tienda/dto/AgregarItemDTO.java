package com.tucasaca.tienda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgregarItemDTO {
    private Long casacaId;
    private Integer cantidad;
}
