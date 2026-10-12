package com.tucasaca.tienda;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tucasaca.tienda.dto.EquipoDTO;
import com.tucasaca.tienda.dto.LigaDTO;
import com.tucasaca.tienda.service.EquipoService;
import com.tucasaca.tienda.service.LigaService;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:ligas-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class LigaIntegrationTest {

    @Autowired
    private LigaService ligaService;

    @Autowired
    private EquipoService equipoService;

    @Test
    void listarLigasRetornaLigasInicializadas() {
        List<LigaDTO> ligas = ligaService.getAllLigas();
        assertNotNull(ligas);
        assertFalse(ligas.isEmpty());
        assertTrue(ligas.stream().anyMatch(l -> l.getNombre().equals("Liga Profesional")));
    }

    @Test
    void listarEquiposRetornaEquiposInicializados() {
        List<EquipoDTO> equipos = equipoService.getAllEquipos();
        assertNotNull(equipos);
        assertFalse(equipos.isEmpty());
        assertTrue(equipos.stream().anyMatch(e -> e.getNombre().equals("San Lorenzo")));
    }

    @Test
    void filtrarEquiposPorLigaRetornaEquiposDeEsaLiga() {
        List<LigaDTO> ligas = ligaService.getAllLigas();
        LigaDTO ligaArg = ligas.stream().filter(l -> l.getNombre().equals("Liga Profesional")).findFirst().orElseThrow();

        List<EquipoDTO> equiposArg = equipoService.getEquiposByLiga(ligaArg.getId());
        assertNotNull(equiposArg);
        assertFalse(equiposArg.isEmpty());
        assertTrue(equiposArg.stream().allMatch(e -> e.getLiga().getId().equals(ligaArg.getId())));
    }
}
