package com.tucasaca.tienda.mapper;

import org.springframework.stereotype.Component;

import com.tucasaca.tienda.dto.CasacaDTO;
import com.tucasaca.tienda.dto.CasacaRequestDTO;
import com.tucasaca.tienda.dto.EquipoDTO;
import com.tucasaca.tienda.dto.LigaDTO;
import com.tucasaca.tienda.model.Casaca;
import com.tucasaca.tienda.model.Equipo;
import com.tucasaca.tienda.model.Liga;

@Component
public class CasacaMapper {

    public LigaDTO toLigaDTO(Liga entity) {
        if (entity == null) {
            return null;
        }
        return new LigaDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getPais()
        );
    }

    public EquipoDTO toEquipoDTO(Equipo entity) {
        if (entity == null) {
            return null;
        }
        return new EquipoDTO(
                entity.getId(),
                entity.getNombre(),
                toLigaDTO(entity.getLiga())
        );
    }

    public CasacaDTO toDTO(Casaca entity) {
        if (entity == null) {
            return null;
        }
        Long creadorId = entity.getCreador() != null ? entity.getCreador().getId() : null;
        String creadorNombreUsuario = entity.getCreador() != null
                ? (entity.getCreador().getNombreUsuario() != null ? entity.getCreador().getNombreUsuario() : entity.getCreador().getEmail())
                : null;

        return new CasacaDTO(
                entity.getId(),
                toEquipoDTO(entity.getEquipo()),
                toLigaDTO(entity.getLiga()),
                entity.getAnio(),
                entity.getJugador(),
                entity.getNumero(),
                entity.getTalle(),
                entity.getPrecio(),
                entity.getStock(),
                entity.getDescripcion(),
                entity.getImagenUrl(),
                creadorId,
                creadorNombreUsuario,
                entity.getActivo(),
                entity.getFechaCreacion()
        );
    }

    public Casaca toEntity(CasacaRequestDTO dto, Equipo equipo, Liga liga) {
        if (dto == null) {
            return null;
        }
        Casaca casaca = new Casaca();
        casaca.setEquipo(equipo);
        casaca.setLiga(liga);
        casaca.setAnio(dto.getAnio());
        casaca.setJugador(dto.getJugador());
        casaca.setNumero(dto.getNumero());
        casaca.setTalle(dto.getTalle());
        casaca.setPrecio(dto.getPrecio());
        casaca.setStock(dto.getStock() != null ? dto.getStock() : 0);
        casaca.setDescripcion(dto.getDescripcion());
        casaca.setImagenUrl(dto.getImagenUrl());
        if (dto.getActivo() != null) {
            casaca.setActivo(dto.getActivo());
        }
        return casaca;
    }
}
