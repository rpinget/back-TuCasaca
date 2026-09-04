package com.tucasaca.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucasaca.tienda.dto.CasacaDTO;
import com.tucasaca.tienda.dto.CasacaRequestDTO;
import com.tucasaca.tienda.exception.PrecioNegativoException;
import com.tucasaca.tienda.exception.ResourceNotFoundException;
import com.tucasaca.tienda.mapper.CasacaMapper;
import com.tucasaca.tienda.model.Casaca;
import com.tucasaca.tienda.model.Equipo;
import com.tucasaca.tienda.model.Liga;
import com.tucasaca.tienda.repository.CasacaRepository;
import com.tucasaca.tienda.repository.EquipoRepository;
import com.tucasaca.tienda.repository.LigaRepository;

@Service
@Transactional(readOnly = true)
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
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la casaca con id: " + id));
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

    @Transactional
    public CasacaDTO saveCasaca(CasacaRequestDTO requestDTO) {
        if (requestDTO.getPrecio() != null
                && requestDTO.getPrecio().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new PrecioNegativoException("El precio no puede ser negativo");
        }

        Equipo equipo = null;
        if (requestDTO.getEquipoId() != null) {
            equipo = equipoRepository.findById(requestDTO.getEquipoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró el equipo con id: " + requestDTO.getEquipoId()));
        }

        Liga liga = null;
        if (requestDTO.getLigaId() != null) {
            liga = ligaRepository.findById(requestDTO.getLigaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró la liga con id: " + requestDTO.getLigaId()));
        }

        Casaca entity = casacaMapper.toEntity(requestDTO, equipo, liga);
        Casaca savedEntity = casacaRepository.save(entity);
        return casacaMapper.toDTO(savedEntity);
    }

    @Transactional
    public CasacaDTO updateCasaca(Long id, CasacaRequestDTO requestDTO) {
        Casaca casaca = casacaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la casaca con id: " + id));

        if (requestDTO.getPrecio() != null
                && requestDTO.getPrecio().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new PrecioNegativoException("El precio no puede ser negativo");
        }

        if (requestDTO.getEquipoId() != null) {
            Equipo equipo = equipoRepository.findById(requestDTO.getEquipoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró el equipo con id: " + requestDTO.getEquipoId()));
            casaca.setEquipo(equipo);
        }

        if (requestDTO.getLigaId() != null) {
            Liga liga = ligaRepository.findById(requestDTO.getLigaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró la liga con id: " + requestDTO.getLigaId()));
            casaca.setLiga(liga);
        }

        if (requestDTO.getAnio() != null) {
            casaca.setAnio(requestDTO.getAnio());
        }
        if (requestDTO.getJugador() != null) {
            casaca.setJugador(requestDTO.getJugador());
        }
        if (requestDTO.getNumero() != null) {
            casaca.setNumero(requestDTO.getNumero());
        }
        if (requestDTO.getTalle() != null) {
            casaca.setTalle(requestDTO.getTalle());
        }
        if (requestDTO.getPrecio() != null) {
            casaca.setPrecio(requestDTO.getPrecio());
        }
        if (requestDTO.getImagenUrl() != null) {
            casaca.setImagenUrl(requestDTO.getImagenUrl());
        }
        if (requestDTO.getActivo() != null) {
            casaca.setActivo(requestDTO.getActivo());
        }

        Casaca updatedEntity = casacaRepository.save(casaca);
        return casacaMapper.toDTO(updatedEntity);
    }

    @Transactional
    public boolean deleteCasaca(Long id) {
        if (!casacaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró la casaca con id: " + id);
        }
        casacaRepository.deleteById(id);
        return true;
    }
}