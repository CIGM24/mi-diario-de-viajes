package com.midiariodeviajes.backend.controller;

import com.midiariodeviajes.backend.dto.RegistroRequest;
import com.midiariodeviajes.backend.dto.RegistroResponse;
import com.midiariodeviajes.backend.model.Usuario;
import com.midiariodeviajes.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.midiariodeviajes.backend.dto.LoginRequest;
import com.midiariodeviajes.backend.dto.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
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

    //Comprueba email y contraseña mediante BCrypt
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return usuarioService.buscarPorEmail(request.getEmail())
                .filter(usuario ->
                        passwordEncoder.matches(
                                request.getPassword(),
                                usuario.getPassword()
                        )
                )
                .map(usuario ->
                        ResponseEntity.ok(
                                new LoginResponse("LOGIN_CORRECTO")
                        )
                )
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                );
    }
}

