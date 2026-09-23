package com.tucasaca.tienda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private void validarAcceso(Long usuarioId, java.security.Principal principal, org.springframework.security.core.Authentication auth) {
        String email = principal != null ? principal.getName() : null;
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        carritoService.validarAccesoUsuario(usuarioId, email, isAdmin);
    }

    // GET http://localhost:8080/api/carritos/{usuarioId}
    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoDTO> obtenerCarrito(
            @PathVariable Long usuarioId,
            java.security.Principal principal,
            org.springframework.security.core.Authentication auth) {
        validarAcceso(usuarioId, principal, auth);
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }

    // POST http://localhost:8080/api/carritos/{usuarioId}/items
    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<CarritoDTO> agregarItem(
            @PathVariable Long usuarioId,
            @jakarta.validation.Valid @RequestBody AgregarItemDTO dto,
            java.security.Principal principal,
            org.springframework.security.core.Authentication auth) {
        validarAcceso(usuarioId, principal, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregarItem(usuarioId, dto));
    }

    // DELETE http://localhost:8080/api/carritos/{usuarioId}/items/{itemId}
    @DeleteMapping("/{usuarioId}/items/{itemId}")
    public ResponseEntity<CarritoDTO> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable Long itemId,
            java.security.Principal principal,
            org.springframework.security.core.Authentication auth) {
        validarAcceso(usuarioId, principal, auth);
        return ResponseEntity.ok(carritoService.eliminarItem(usuarioId, itemId));
    }

    // DELETE http://localhost:8080/api/carritos/{usuarioId}/vaciar
    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<CarritoDTO> vaciarCarrito(
            @PathVariable Long usuarioId,
            java.security.Principal principal,
            org.springframework.security.core.Authentication auth) {
        validarAcceso(usuarioId, principal, auth);
        return ResponseEntity.ok(carritoService.vaciarCarrito(usuarioId));
    }

    // POST http://localhost:8080/api/carritos/{usuarioId}/checkout
    @PostMapping("/{usuarioId}/checkout")
    public ResponseEntity<CarritoDTO> checkout(
            @PathVariable Long usuarioId,
            java.security.Principal principal,
            org.springframework.security.core.Authentication auth) {
        validarAcceso(usuarioId, principal, auth);
        return ResponseEntity.ok(carritoService.checkout(usuarioId));
    }
}
