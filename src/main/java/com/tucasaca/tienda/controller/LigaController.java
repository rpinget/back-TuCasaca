package com.tucasaca.tienda.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.dto.LigaDTO;
import com.tucasaca.tienda.service.LigaService;

// http://localhost:8080/api/ligas
@RestController
@RequestMapping("/api/ligas")
@CrossOrigin(origins = "*")
public class LigaController {

    private final LigaService ligaService;

    public LigaController(LigaService ligaService) {
        this.ligaService = ligaService;
    }

    // GET http://localhost:8080/api/ligas
    @GetMapping
    public ResponseEntity<List<LigaDTO>> getAllLigas() {
        return ResponseEntity.ok(ligaService.getAllLigas());
    }

    // GET http://localhost:8080/api/ligas/1
    @GetMapping("/{id}")
    public ResponseEntity<LigaDTO> getLigaById(@PathVariable Long id) {
        LigaDTO liga = ligaService.getLigaById(id);
        if (liga == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(liga);
    }
}
