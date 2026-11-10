package com.midiariodeviajes.backend.service;

import com.midiariodeviajes.backend.model.EstadoViaje;
import com.midiariodeviajes.backend.model.Viaje;
import com.midiariodeviajes.backend.repository.LugarRepository;
import com.midiariodeviajes.backend.repository.ViajeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViajeService.class);

    private final ViajeRepository viajeRepository;
    private final LugarRepository lugarRepository;
    private final AlmacenamientoFotosService almacenamientoFotosService;

    public ViajeService(ViajeRepository viajeRepository,
                        LugarRepository lugarRepository,
                        AlmacenamientoFotosService almacenamientoFotosService) {
        this.viajeRepository = viajeRepository;
        this.lugarRepository = lugarRepository;
        this.almacenamientoFotosService = almacenamientoFotosService;
    }

    public List<Viaje> obtenerPorUsuario(String usuarioId) {
        return viajeRepository.findByUsuarioId(usuarioId);
    }

    public List<Viaje> obtenerPorUsuarioYEstado(
            String usuarioId,
            EstadoViaje estado) {

        return viajeRepository.findByUsuarioIdAndEstado(usuarioId, estado);
    }

    public Optional<Viaje> obtenerPorIdYUsuario(
            String id,
            String usuarioId) {

        return viajeRepository.findById(id)
                .filter(viaje -> viaje.getUsuarioId().equals(usuarioId));
    }

    public Viaje guardar(Viaje viaje) {
        return viajeRepository.save(viaje);
    }

    public boolean eliminar(String id, String usuarioId) {
        Optional<Viaje> viaje = obtenerPorIdYUsuario(id, usuarioId);

        if (viaje.isEmpty()) {
            return false;
        }

        Viaje viajeAEliminar = viaje.get();
        String fotoUrl = viajeAEliminar.getFotoUrl();

        lugarRepository.deleteAll(
                lugarRepository.findByViajeId(id)
        );

        viajeRepository.delete(viajeAEliminar);

        if (fotoUrl != null && !fotoUrl.isBlank()) {
            try {
                almacenamientoFotosService.eliminar(fotoUrl);
            } catch (IOException | IllegalArgumentException errorLimpieza) {
                LOGGER.warn("No se pudo eliminar la foto {} del viaje eliminado {}.", fotoUrl, id, errorLimpieza);
            }
        }

        return true;
    }

    public Optional<Viaje> obtenerPorFotoYUsuario(
            String nombreArchivo,
            String usuarioId) {
        return viajeRepository.findByFotoUrlAndUsuarioId(nombreArchivo, usuarioId);
    }
}
