package com.tucasaca.tienda;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.http.Cookie;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:cookie-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CookieAuthIntegrationTest {

    private static final String LOGIN_BODY = "{\"email\":\"ana.gomez@tucasaca.com\",\"password\":\"usuario123\"}";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private MvcResult login() throws Exception {
        return mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(LOGIN_BODY))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void loginSeteaCookieHttpOnlyYNoExponeElTokenEnElCuerpo() throws Exception {
        MvcResult result = login();

        String setCookie = result.getResponse().getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(setCookie);
        assertTrue(setCookie.startsWith("access_token="));
        assertTrue(setCookie.contains("HttpOnly"));
        assertTrue(setCookie.contains("Secure"));
        assertTrue(setCookie.contains("SameSite=Lax"));
        assertTrue(setCookie.contains("Path=/"));
        assertFalse(result.getResponse().getContentAsString().contains("token"));
    }

    @Test
    void endpointProtegidoFuncionaConLaCookie() throws Exception {
        Cookie cookie = login().getResponse().getCookie("access_token");
        assertNotNull(cookie);

        mockMvc.perform(get("/api/usuarios/me").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana.gomez@tucasaca.com"));
    }

    @Test
    void sinCookieNiHeaderElEndpointProtegidoRechazaLaPeticion() throws Exception {
        int s = mockMvc.perform(get("/api/usuarios/me")).andReturn().getResponse().getStatus();
        assertTrue(s == 401 || s == 403, "esperaba 401/403 y fue " + s);
    }

    @Test
    void cookieConTokenAlteradoEsRechazada() throws Exception {
        Cookie cookie = login().getResponse().getCookie("access_token");
        Cookie alterada = new Cookie("access_token", cookie.getValue() + "x");

        int s = mockMvc.perform(get("/api/usuarios/me").cookie(alterada)).andReturn().getResponse().getStatus();
        assertTrue(s == 401 || s == 403, "esperaba 401/403 y fue " + s);
    }

    @Test
    void logoutExpiraLaCookie() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isNoContent())
                .andReturn();

        String setCookie = result.getResponse().getHeader(HttpHeaders.SET_COOKIE);
        assertNotNull(setCookie);
        assertTrue(setCookie.startsWith("access_token=;"));
        assertTrue(setCookie.contains("Max-Age=0"));
    }
}
