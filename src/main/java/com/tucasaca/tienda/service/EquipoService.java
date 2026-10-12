package com.tucasaca.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucasaca.tienda.dto.EquipoDTO;
import com.tucasaca.tienda.exception.ResourceNotFoundException;
import com.tucasaca.tienda.mapper.CasacaMapper;
import com.tucasaca.tienda.repository.EquipoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final CasacaMapper casacaMapper;

    public List<EquipoDTO> getAllEquipos() {
        return equipoRepository.findAll().stream()
                .map(casacaMapper::toEquipoDTO)
                .toList();
    }

    public EquipoDTO getEquipoById(Long id) {
        return equipoRepository.findById(id)
                .map(casacaMapper::toEquipoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el equipo con id: " + id));
    }

    public List<EquipoDTO> getEquiposByLiga(Long ligaId) {
        return equipoRepository.findByLigaId(ligaId).stream()
                .map(casacaMapper::toEquipoDTO)
                .toList();
    }
}
