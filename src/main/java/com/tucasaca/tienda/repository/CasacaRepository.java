package com.tucasaca.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tucasaca.tienda.model.Casaca;

public interface CasacaRepository extends JpaRepository<Casaca, Long> {

    List<Casaca> findByEquipo(String equipo);

    List<Casaca> findByEquipoContainingIgnoreCase(String equipo);
}