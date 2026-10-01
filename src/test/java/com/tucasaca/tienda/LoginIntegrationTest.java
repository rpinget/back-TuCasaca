package com.tucasaca.tienda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tucasaca.tienda.dto.LoginRequestDTO;
import com.tucasaca.tienda.dto.LoginResponseDTO;
import com.tucasaca.tienda.dto.UsuarioResponseDTO;
import com.tucasaca.tienda.service.AuthenticationService;
import com.tucasaca.tienda.service.UsuarioService;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:login-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class LoginIntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void loginRetornaTokenYDatosDeUsuario() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("ana.gomez@tucasaca.com", "usuario123");
        LoginResponseDTO response = authenticationService.authenticate(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getId());
        assertEquals("ana.gomez@tucasaca.com", response.getEmail());
        assertEquals("Ana", response.getNombre());
        assertEquals("USUARIO", response.getRol());
    }

    @Test
    void obtenerPerfilPorEmailRetornaDatosCorrectos() {
        UsuarioResponseDTO perfil = usuarioService.obtenerPerfilPorEmail("admin@tucasaca.com");

        assertNotNull(perfil);
        assertEquals("admin@tucasaca.com", perfil.getEmail());
        assertEquals("Admin", perfil.getNombre());
    }
}
