package com.tucasaca.tienda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.dto.AgregarItemDTO;
import com.tucasaca.tienda.dto.CarritoDTO;
import com.tucasaca.tienda.service.CarritoService;

// http://localhost:8080/api/carritos
@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // GET http://localhost:8080/api/carritos/{usuarioId}
    @GetMapping("/{usuarioId}")
    public CarritoDTO obtenerCarrito(@PathVariable Long usuarioId) {
        return carritoService.obtenerCarrito(usuarioId);
    }

    // POST http://localhost:8080/api/carritos/{usuarioId}/items
    @PostMapping("/{usuarioId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoDTO agregarItem(@PathVariable Long usuarioId, @RequestBody AgregarItemDTO dto) {
        return carritoService.agregarItem(usuarioId, dto);
    }

    // DELETE http://localhost:8080/api/carritos/{usuarioId}/items/{itemId}
    @DeleteMapping("/{usuarioId}/items/{itemId}")
    public CarritoDTO eliminarItem(@PathVariable Long usuarioId, @PathVariable Long itemId) {
        return carritoService.eliminarItem(usuarioId, itemId);
    }

    // DELETE http://localhost:8080/api/carritos/{usuarioId}/vaciar
    @DeleteMapping("/{usuarioId}/vaciar")
    public CarritoDTO vaciarCarrito(@PathVariable Long usuarioId) {
        return carritoService.vaciarCarrito(usuarioId);
    }

    // POST http://localhost:8080/api/carritos/{usuarioId}/checkout
    @PostMapping("/{usuarioId}/checkout")
    public CarritoDTO checkout(@PathVariable Long usuarioId) {
        return carritoService.checkout(usuarioId);
    }
}
