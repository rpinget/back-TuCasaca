package com.tucasaca.tienda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tucasaca.tienda.dto.UsuarioRegistroDTO;
import com.tucasaca.tienda.dto.UsuarioResponseDTO;
import com.tucasaca.tienda.model.Usuario;
import com.tucasaca.tienda.repository.UsuarioRepository;
import com.tucasaca.tienda.service.UsuarioService;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:usuarios-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UsuarioRegistroIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void registraUsuarioYVerificaDatosEnBase() {
        UsuarioRegistroDTO registro = new UsuarioRegistroDTO(
                "Ana",
                "Torres",
                "ana.torres@example.com",
                "secreto",
                LocalDate.of(1995, 6, 15),
                "FEMENINO");

        UsuarioResponseDTO respuesta = usuarioService.registrarUsuario(registro);

        assertNotNull(respuesta.getId());
        assertEquals(registro.getEmail(), respuesta.getEmail());

        Usuario usuarioEnBase = usuarioRepository.findByEmail(registro.getEmail()).orElseThrow();
        assertEquals(registro.getFechaNacimiento(), usuarioEnBase.getFechaNacimiento());
        assertEquals(registro.getSexo(), usuarioEnBase.getSexo());
        assertEquals("USER", usuarioEnBase.getRol());
        assertEquals(true, usuarioEnBase.getActivo());
        assertEquals(registro.getPassword(), usuarioEnBase.getPassword());
    }
}