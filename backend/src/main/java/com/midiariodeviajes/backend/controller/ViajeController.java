package com.midiariodeviajes.backend.controller;

import com.midiariodeviajes.backend.model.Viaje;
import com.midiariodeviajes.backend.service.ViajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    @GetMapping
    public ResponseEntity<List<Viaje>> obtenerViajes(Authentication authentication) {

        String usuarioId = authentication.getName();

        List<Viaje> viajes = viajeService.obtenerPorUsuario(usuarioId);

        return ResponseEntity.ok(viajes);
    }
}
