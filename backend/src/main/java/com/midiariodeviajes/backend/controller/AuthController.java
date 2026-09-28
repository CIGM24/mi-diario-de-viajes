package com.midiariodeviajes.backend.controller;

import com.midiariodeviajes.backend.dto.RegistroRequest;
import com.midiariodeviajes.backend.dto.RegistroResponse;
import com.midiariodeviajes.backend.model.Usuario;
import com.midiariodeviajes.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistroResponse> registrar(@Valid @RequestBody RegistroRequest request) {

        if (usuarioService.existeEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Usuario usuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                request.getPassword()
        );

        Usuario usuarioGuardado = usuarioService.guardar(usuario);

        RegistroResponse response = new RegistroResponse(
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

