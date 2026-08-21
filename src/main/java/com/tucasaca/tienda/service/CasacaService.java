package com.tucasaca.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tucasaca.tienda.dto.CasacaDTO;
import com.tucasaca.tienda.dto.CasacaRequestDTO;
import com.tucasaca.tienda.mapper.CasacaMapper;
import com.tucasaca.tienda.model.Casaca;
import com.tucasaca.tienda.model.Equipo;
import com.tucasaca.tienda.model.Liga;
import com.tucasaca.tienda.repository.CasacaRepository;
import com.tucasaca.tienda.repository.EquipoRepository;
import com.tucasaca.tienda.repository.LigaRepository;

@Service
public class CasacaService {

    private final CasacaRepository casacaRepository;
    private final EquipoRepository equipoRepository;
    private final LigaRepository ligaRepository;
    private final CasacaMapper casacaMapper;

    public CasacaService(
            CasacaRepository casacaRepository,
            EquipoRepository equipoRepository,
            LigaRepository ligaRepository,
            CasacaMapper casacaMapper) {
        this.casacaRepository = casacaRepository;
        this.equipoRepository = equipoRepository;
        this.ligaRepository = ligaRepository;
        this.casacaMapper = casacaMapper;
    }

    public List<CasacaDTO> getAllCasacas() {
        return casacaRepository.findAll().stream()
                .map(casacaMapper::toDTO)
                .toList();
    }

    public CasacaDTO getCasacaById(Long id) {
        return casacaRepository.findById(id)
                .map(casacaMapper::toDTO)
                .orElse(null);
    }

    public List<CasacaDTO> getCasacasByEquipo(String equipo) {
        return casacaRepository.findByEquipoNombreContainingIgnoreCase(equipo).stream()
                .map(casacaMapper::toDTO)
                .toList();
    }

    public List<CasacaDTO> getCasacasByLiga(Long ligaId) {
        return casacaRepository.findByLigaId(ligaId).stream()
                .map(casacaMapper::toDTO)
                .toList();
    }

    public List<CasacaDTO> getCasacasByLigaNombre(String ligaNombre) {
        return casacaRepository.findByLigaNombreContainingIgnoreCase(ligaNombre).stream()
                .map(casacaMapper::toDTO)
                .toList();
    }

    public CasacaDTO saveCasaca(CasacaRequestDTO requestDTO) {
        Equipo equipo = null;
        if (requestDTO.getEquipoId() != null) {
            equipo = equipoRepository.findById(requestDTO.getEquipoId()).orElse(null);
        }

        Liga liga = null;
        if (requestDTO.getLigaId() != null) {
            liga = ligaRepository.findById(requestDTO.getLigaId()).orElse(null);
        }

        Casaca entity = casacaMapper.toEntity(requestDTO, equipo, liga);
        Casaca savedEntity = casacaRepository.save(entity);
        return casacaMapper.toDTO(savedEntity);
    }
}