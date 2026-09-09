package com.tucasaca.tienda.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ligas")
public class Liga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "pais", length = 100)
    private String pais;

    @OneToMany(mappedBy = "liga", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "liga", "casacas" })
    private List<Equipo> equipos = new ArrayList<>();

    @OneToMany(mappedBy = "liga")
    @JsonIgnoreProperties({ "liga", "equipo" })
    private List<Casaca> casacas = new ArrayList<>();
}
