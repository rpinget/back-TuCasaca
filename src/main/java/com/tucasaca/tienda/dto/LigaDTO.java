package com.tucasaca.tienda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigaDTO {
    private Long id;
    private String nombre;
    private String pais;
}
