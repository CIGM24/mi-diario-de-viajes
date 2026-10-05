package com.midiariodeviajes.backend.controller;

import com.midiariodeviajes.backend.dto.LugarRequest;
import com.midiariodeviajes.backend.model.Lugar;
import com.midiariodeviajes.backend.service.LugarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LugarController {

    private final LugarService lugarService;

    public LugarController(LugarService lugarService) {
        this.lugarService = lugarService;
    }

    @GetMapping("/viajes/{viajeId}/lugares")
    public ResponseEntity<List<Lugar>> obtenerLugares(
            @PathVariable String viajeId,
            Authentication authentication) {

        String usuarioId = authentication.getName();

        List<Lugar> lugares = lugarService.obtenerPorViaje(viajeId, usuarioId);

        return ResponseEntity.ok(lugares);
    }

    @PostMapping("/viajes/{viajeId}/lugares")
    public ResponseEntity<Lugar> crearLugar(
            @PathVariable String viajeId,
            @Valid @RequestBody LugarRequest request,
            Authentication authentication) {

        Lugar lugar = new Lugar(
                viajeId,
                request.getNombre(),
                request.getDescripcion(),
                request.getDireccion(),
                request.getLatitud(),
                request.getLongitud()
        );

        String usuarioId = authentication.getName();

        return lugarService.guardar(lugar, usuarioId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/lugares/{id}")
    public ResponseEntity<Lugar> actualizarLugar(
            @PathVariable String id,
            @Valid @RequestBody LugarRequest request,
            Authentication authentication) {

        String usuarioId = authentication.getName();

        return lugarService.obtenerPorId(id, usuarioId)
                .map(lugar -> {
                    lugar.setNombre(request.getNombre());
                    lugar.setDescripcion(request.getDescripcion());
                    lugar.setDireccion(request.getDireccion());
                    lugar.setLatitud(request.getLatitud());
                    lugar.setLongitud(request.getLongitud());

                    return lugarService.guardar(lugar, usuarioId)
                            .map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}