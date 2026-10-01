package com.tucasaca.tienda.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucasaca.tienda.dto.LigaDTO;
import com.tucasaca.tienda.mapper.CasacaMapper;
import com.tucasaca.tienda.repository.LigaRepository;

@Service
@Transactional(readOnly = true)
public class LigaService {

    private final LigaRepository ligaRepository;
    private final CasacaMapper casacaMapper;

    public LigaService(LigaRepository ligaRepository, CasacaMapper casacaMapper) {
        this.ligaRepository = ligaRepository;
        this.casacaMapper = casacaMapper;
    }

    public List<LigaDTO> getAllLigas() {
        return ligaRepository.findAll().stream()
                .map(casacaMapper::toLigaDTO)
                .toList();
    }

    public LigaDTO getLigaById(Long id) {
        return ligaRepository.findById(id)
                .map(casacaMapper::toLigaDTO)
                .orElse(null);
    }
}
