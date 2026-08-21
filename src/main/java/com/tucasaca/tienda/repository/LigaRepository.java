package com.tucasaca.tienda.repository;

import com.tucasaca.tienda.model.Liga;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LigaRepository extends JpaRepository<Liga, Long> {
    Optional<Liga> findByNombre(String nombre);
}
