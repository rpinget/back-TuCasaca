package com.tucasaca.tienda.repository;

import com.tucasaca.tienda.model.Liga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LigaRepository extends JpaRepository<Liga, Long> {
    Optional<Liga> findByNombre(String nombre);
}
