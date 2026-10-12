package com.tucasaca.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucasaca.tienda.dto.LigaDTO;
import com.tucasaca.tienda.exception.ResourceNotFoundException;
import com.tucasaca.tienda.mapper.CasacaMapper;
import com.tucasaca.tienda.repository.LigaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LigaService {

    private final LigaRepository ligaRepository;
    private final CasacaMapper casacaMapper;

    public List<LigaDTO> getAllLigas() {
        return ligaRepository.findAll().stream()
                .map(casacaMapper::toLigaDTO)
                .toList();
    }

    public LigaDTO getLigaById(Long id) {
        return ligaRepository.findById(id)
                .map(casacaMapper::toLigaDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la liga con id: " + id));
    }
}
