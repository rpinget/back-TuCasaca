package com.tucasaca.tienda.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "casacas")
public class Casaca {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "equipo", nullable = false, length = 100)
	private String equipo;

	@Column(name = "liga", length = 100)
	private String liga;

	@Column(name = "anio")
	private Integer anio;

	@Column(name = "jugador", length = 100)
	private String jugador;

	@Column(name = "numero")
	private Integer numero;

	@Column(name = "precio", nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;

	@Column(name = "imagen_url", length = 255)
	private String imagenUrl;

	@Column(nullable = false)
	private Boolean activo = true;

	@Column(name = "fecha_creacion", nullable = false, updatable = false)
	private LocalDateTime fechaCreacion = LocalDateTime.now();

}