package com.tucasaca.tienda.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tucasaca.tienda.dto.UsuarioRegistroDTO;
import com.tucasaca.tienda.dto.UsuarioResponseDTO;
import com.tucasaca.tienda.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@RequestBody UsuarioRegistroDTO registroDTO) {
        UsuarioResponseDTO usuario = usuarioService.registrarUsuario(registroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
}