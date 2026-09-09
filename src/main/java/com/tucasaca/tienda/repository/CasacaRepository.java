package com.tucasaca.tienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tucasaca.tienda.model.Casaca;

@Repository
public interface CasacaRepository extends JpaRepository<Casaca, Long> {

    List<Casaca> findByEquipoNombre(String equipo);

    List<Casaca> findByEquipoNombreContainingIgnoreCase(String equipo);

    List<Casaca> findByLigaId(Long ligaId);

    List<Casaca> findByLigaNombreContainingIgnoreCase(String ligaNombre);
}