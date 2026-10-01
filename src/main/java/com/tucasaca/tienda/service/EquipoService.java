package com.tucasaca.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucasaca.tienda.dto.EquipoDTO;
import com.tucasaca.tienda.mapper.CasacaMapper;
import com.tucasaca.tienda.repository.EquipoRepository;

@Service
@Transactional(readOnly = true)
public class EquipoService {

    private final EquipoRepository equipoRepository;
    private final CasacaMapper casacaMapper;

    public EquipoService(EquipoRepository equipoRepository, CasacaMapper casacaMapper) {
        this.equipoRepository = equipoRepository;
        this.casacaMapper = casacaMapper;
    }

    public List<EquipoDTO> getAllEquipos() {
        return equipoRepository.findAll().stream()
                .map(casacaMapper::toEquipoDTO)
                .toList();
    }

    public EquipoDTO getEquipoById(Long id) {
        return equipoRepository.findById(id)
                .map(casacaMapper::toEquipoDTO)
                .orElse(null);
    }

    public List<EquipoDTO> getEquiposByLiga(Long ligaId) {
        return equipoRepository.findByLigaId(ligaId).stream()
                .map(casacaMapper::toEquipoDTO)
                .toList();
    }
}
