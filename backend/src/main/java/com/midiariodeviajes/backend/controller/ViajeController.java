package com.midiariodeviajes.backend.controller;

import com.midiariodeviajes.backend.dto.ViajeRequest;
import com.midiariodeviajes.backend.model.Viaje;
import com.midiariodeviajes.backend.service.ViajeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerViaje(
            @PathVariable String id,
            Authentication authentication) {

        String usuarioId = authentication.getName();

        return viajeService.obtenerPorIdYUsuario(id, usuarioId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Viaje> actualizarViaje(
            @PathVariable String id,
            @Valid @RequestBody ViajeRequest request,
            Authentication authentication) {

        String usuarioId = authentication.getName();

        return viajeService.obtenerPorIdYUsuario(id, usuarioId)
                .map(viaje -> {
                    viaje.setNombre(request.getNombre());
                    viaje.setDestino(request.getDestino());
                    viaje.setFechaInicio(request.getFechaInicio());
                    viaje.setFechaFin(request.getFechaFin());
                    viaje.setPresupuesto(request.getPresupuesto());
                    viaje.setDescripcion(request.getDescripcion());
                    viaje.setEstado(request.getEstado());

                    Viaje viajeActualizado = viajeService.guardar(viaje);

                    return ResponseEntity.ok(viajeActualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarViaje(
            @PathVariable String id,
            Authentication authentication) {

        String usuarioId = authentication.getName();

        boolean eliminado = viajeService.eliminar(id, usuarioId);

        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Viaje> crearViaje(
            @Valid @RequestBody ViajeRequest request,
            Authentication authentication) {

        String usuarioId = authentication.getName();

        Viaje viaje = new Viaje(
                usuarioId,
                request.getNombre(),
                request.getDestino(),
                request.getFechaInicio(),
                request.getFechaFin(),
                request.getPresupuesto(),
                request.getDescripcion(),
                request.getEstado()
        );
        Viaje viajeGuardado = viajeService.guardar(viaje);

        return ResponseEntity.ok(viajeGuardado);

    }
}