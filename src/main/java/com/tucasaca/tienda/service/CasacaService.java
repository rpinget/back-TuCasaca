package com.tucasaca.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tucasaca.tienda.model.Casaca;
import com.tucasaca.tienda.repository.CasacaRepository;

@Service
public class CasacaService {

    private final CasacaRepository casacaRepository;

    CasacaService(CasacaRepository casacaRepository) {
        this.casacaRepository = casacaRepository;
    }

    public List<Casaca> getAllCasacas() {
        return casacaRepository.findAll();
    }

    public Casaca getCasacaById(Long id) {
        return casacaRepository.findById(id).orElse(null);
    }

    public List<Casaca> getCasacasByEquipo(String equipo) {
        return casacaRepository.findByEquipoContainingIgnoreCase(equipo);
    }
}