package com.tucasaca.tienda.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.dto.EquipoDTO;
import com.tucasaca.tienda.service.EquipoService;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/api/equipos
@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService equipoService;

    // GET http://localhost:8080/api/equipos
    // GET http://localhost:8080/api/equipos?ligaId=1
    @GetMapping
    public ResponseEntity<List<EquipoDTO>> getAllEquipos(@RequestParam(required = false) Long ligaId) {
        if (ligaId != null) {
            return ResponseEntity.ok(equipoService.getEquiposByLiga(ligaId));
        }
        return ResponseEntity.ok(equipoService.getAllEquipos());
    }

    // GET http://localhost:8080/api/equipos/1
    @GetMapping("/{id}")
    public ResponseEntity<EquipoDTO> getEquipoById(@PathVariable Long id) {
        return ResponseEntity.ok(equipoService.getEquipoById(id));
    }

    // GET http://localhost:8080/api/equipos/liga/1
    @GetMapping("/liga/{ligaId}")
    public ResponseEntity<List<EquipoDTO>> getEquiposByLiga(@PathVariable Long ligaId) {
        return ResponseEntity.ok(equipoService.getEquiposByLiga(ligaId));
    }
}
