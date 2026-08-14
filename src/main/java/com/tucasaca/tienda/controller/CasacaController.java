package com.tucasaca.tienda.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.model.Casaca;
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
    public List<Casaca> getAllCasacas() {
        return casacaService.getAllCasacas();
    }

    // get http://localhost:8080/api/casacas/1
    @GetMapping("/{id}")
    public Casaca getCasacaById(@PathVariable Long id) {
        return casacaService.getCasacaById(id);
    }

    // get http://localhost:8080/api/casacas/equipo/San Lorenzo
    @GetMapping("/equipo/{equipo}")
    public List<Casaca> getCasacasByEquipo(@PathVariable String equipo) {
        return casacaService.getCasacasByEquipo(equipo);
    }
}