package com.tucasaca.tienda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.tucasaca.tienda.dto.ApiErrorDTO;
import com.tucasaca.tienda.exception.GlobalExceptionHandler;
import com.tucasaca.tienda.exception.PrecioNegativoException;
import com.tucasaca.tienda.exception.ResourceNotFoundException;
import com.tucasaca.tienda.exception.StockInsuficienteException;

class ApiErrorTest {

    private GlobalExceptionHandler exceptionHandler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/casacas/99");
    }

    @Test
    void manejarResourceNoEncontradoRetornaApiErrorDTOCon404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("No se encontró la casaca");
        ResponseEntity<ApiErrorDTO> response = exceptionHandler.manejarResourceNoEncontrado(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("No se encontró la casaca", response.getBody().getMessage());
        assertEquals("/api/casacas/99", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void manejarPrecioNegativoRetornaApiErrorDTOCon400() {
        PrecioNegativoException ex = new PrecioNegativoException("El precio no puede ser negativo");
        ResponseEntity<ApiErrorDTO> response = exceptionHandler.manejarPrecioNegativo(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("El precio no puede ser negativo", response.getBody().getMessage());
    }

    @Test
    void manejarStockInsuficienteRetornaApiErrorDTOCon400() {
        StockInsuficienteException ex = new StockInsuficienteException("Stock insuficiente");
        ResponseEntity<ApiErrorDTO> response = exceptionHandler.manejarStockInsuficiente(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Stock insuficiente", response.getBody().getMessage());
    }
}
