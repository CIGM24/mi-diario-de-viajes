package com.midiariodeviajes.backend.controller;

import com.midiariodeviajes.backend.service.AlmacenamientoFotosService;
import com.midiariodeviajes.backend.service.ViajeService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Locale;

@RestController
public class FotoController {

    private final ViajeService viajeService;
    private final AlmacenamientoFotosService almacenamientoFotosService;

    public FotoController(
            ViajeService viajeService,
            AlmacenamientoFotosService almacenamientoFotosService) {
        this.viajeService = viajeService;
        this.almacenamientoFotosService = almacenamientoFotosService;
    }

    @GetMapping("/fotos/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerFoto(
            @PathVariable String nombreArchivo,
            Authentication authentication) {

        String usuarioId = authentication.getName();

        if (viajeService.obtenerPorFotoYUsuario(nombreArchivo, usuarioId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource foto = almacenamientoFotosService.cargar(nombreArchivo);

            return ResponseEntity.ok()
                    .contentType(tipoContenido(nombreArchivo))
                    .body(foto);
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType tipoContenido(String nombreArchivo) {
        String nombre = nombreArchivo.toLowerCase(Locale.ROOT);

        if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }
        if (nombre.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (nombre.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        }

        return MediaType.APPLICATION_OCTET_STREAM;
    }
}

