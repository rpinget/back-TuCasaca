package com.tucasaca.tienda.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.dto.LigaDTO;
import com.tucasaca.tienda.service.LigaService;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/api/ligas
@RestController
@RequestMapping("/api/ligas")
@RequiredArgsConstructor
public class LigaController {

    private final LigaService ligaService;

    // GET http://localhost:8080/api/ligas
    @GetMapping
    public ResponseEntity<List<LigaDTO>> getAllLigas() {
        return ResponseEntity.ok(ligaService.getAllLigas());
    }

    // GET http://localhost:8080/api/ligas/1
    @GetMapping("/{id}")
    public ResponseEntity<LigaDTO> getLigaById(@PathVariable Long id) {
        return ResponseEntity.ok(ligaService.getLigaById(id));
    }
}
