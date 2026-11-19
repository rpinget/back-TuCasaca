package com.tucasaca.tienda;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:seguridad-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class SeguridadIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String loginYObtenerToken(String email, String password) throws Exception {
        String body = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Matcher m = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
        if (!m.find()) throw new AssertionError("La respuesta del login no trae token: " + body);
        return m.group(1);
    }

    @Test
    void rutaProtegidaSinTokenDevuelve401ConApiError() throws Exception {
        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.path").value("/api/usuarios/me"));
    }

    @Test
    void rutaProtegidaConTokenInvalidoDevuelve401() throws Exception {
        mockMvc.perform(get("/api/usuarios/me").header("Authorization", "Bearer token.invalido.123"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void rutaProtegidaConTokenValidoDevuelve200() throws Exception {
        String token = loginYObtenerToken("ana.gomez@tucasaca.com", "usuario123");
        mockMvc.perform(get("/api/usuarios/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana.gomez@tucasaca.com"));
    }

    @Test
    void usuarioSinRolAdminEnRutaAdminDevuelve403() throws Exception {
        String token = loginYObtenerToken("ana.gomez@tucasaca.com", "usuario123");
        mockMvc.perform(delete("/api/usuarios/admin/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void loginDeUsuarioDadoDeBajaDevuelve401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"luis.baja@tucasaca.com\",\"password\":\"usuario123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("El usuario está deshabilitado"));
    }

    @Test
    void loginConPasswordIncorrectaSigueDevolviendo401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ana.gomez@tucasaca.com\",\"password\":\"mala\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }
}
