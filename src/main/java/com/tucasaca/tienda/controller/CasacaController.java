package com.tucasaca.tienda.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tucasaca.tienda.dto.CasacaDTO;
import com.tucasaca.tienda.dto.CasacaRequestDTO;
import com.tucasaca.tienda.service.CasacaService;

// http://localhost:8080/api/casacas
@RestController
@RequestMapping("/api/casacas")
public class CasacaController {

    private final CasacaService casacaService;

    public CasacaController(CasacaService casacaService) {
        this.casacaService = casacaService;
    }

    // GET http://localhost:8080/api/casacas
    @GetMapping
    public ResponseEntity<List<CasacaDTO>> getAllCasacas() {
        return ResponseEntity.ok(casacaService.getAllCasacas());
    }

    // GET http://localhost:8080/api/casacas/1
    @GetMapping("/{id}")
    public ResponseEntity<CasacaDTO> getCasacaById(@PathVariable Long id) {
        return ResponseEntity.ok(casacaService.getCasacaById(id));
    }

    // GET http://localhost:8080/api/casacas/equipo/San Lorenzo
    @GetMapping("/equipo/{equipo}")
    public ResponseEntity<List<CasacaDTO>> getCasacasByEquipo(@PathVariable String equipo) {
        return ResponseEntity.ok(casacaService.getCasacasByEquipo(equipo));
    }

    // GET http://localhost:8080/api/casacas/liga/1
    @GetMapping("/liga/{ligaId}")
    public ResponseEntity<List<CasacaDTO>> getCasacasByLiga(@PathVariable Long ligaId) {
        return ResponseEntity.ok(casacaService.getCasacasByLiga(ligaId));
    }

    // GET http://localhost:8080/api/casacas/liga/nombre/Liga Profesional
    @GetMapping("/liga/nombre/{nombre}")
    public ResponseEntity<List<CasacaDTO>> getCasacasByLigaNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(casacaService.getCasacasByLigaNombre(nombre));
    }

    // POST http://localhost:8080/api/casacas
    @PostMapping
    public ResponseEntity<CasacaDTO> createCasaca(@RequestBody CasacaRequestDTO casacaRequestDTO) {
        CasacaDTO nuevaCasaca = casacaService.saveCasaca(casacaRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaCasaca.getId())
                .toUri();
        return ResponseEntity.created(location).body(nuevaCasaca);
    }

    // PUT http://localhost:8080/api/casacas/1
    @PutMapping("/{id}")
    public ResponseEntity<CasacaDTO> updateCasaca(
            @PathVariable Long id,
            @RequestBody CasacaRequestDTO casacaRequestDTO) {
        return ResponseEntity.ok(casacaService.updateCasaca(id, casacaRequestDTO));
    }

    // DELETE http://localhost:8080/api/casacas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCasaca(@PathVariable Long id) {
        casacaService.deleteCasaca(id);
        return ResponseEntity.noContent().build();
    }
}