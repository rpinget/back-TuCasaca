package com.tucasaca.tienda.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.dto.CasacaDTO;
import com.tucasaca.tienda.dto.CasacaRequestDTO;
import com.tucasaca.tienda.service.CasacaService;

// http://localhost:8080/api/casacas
@RestController
@RequestMapping("/api/casacas")
public class CasacaController {

    private final CasacaService casacaService;

    CasacaController(CasacaService casacaService) {
        this.casacaService = casacaService;
    }

    // get http://localhost:8080/api/casacas
    @GetMapping
    public List<CasacaDTO> getAllCasacas() {
        return casacaService.getAllCasacas();
    }

    // get http://localhost:8080/api/casacas/1
    @GetMapping("/{id}")
    public CasacaDTO getCasacaById(@PathVariable Long id) {
        return casacaService.getCasacaById(id);
    }

    // get http://localhost:8080/api/casacas/equipo/San Lorenzo
    @GetMapping("/equipo/{equipo}")
    public List<CasacaDTO> getCasacasByEquipo(@PathVariable String equipo) {
        return casacaService.getCasacasByEquipo(equipo);
    }

    // get http://localhost:8080/api/casacas/liga/1
    @GetMapping("/liga/{ligaId}")
    public List<CasacaDTO> getCasacasByLiga(@PathVariable Long ligaId) {
        return casacaService.getCasacasByLiga(ligaId);
    }

    // get http://localhost:8080/api/casacas/liga/nombre/Liga Profesional
    @GetMapping("/liga/nombre/{nombre}")
    public List<CasacaDTO> getCasacasByLigaNombre(@PathVariable String nombre) {
        return casacaService.getCasacasByLigaNombre(nombre);
    }

    // post http://localhost:8080/api/casacas
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CasacaDTO createCasaca(@RequestBody CasacaRequestDTO casacaRequestDTO) {
        return casacaService.saveCasaca(casacaRequestDTO);
    }
}