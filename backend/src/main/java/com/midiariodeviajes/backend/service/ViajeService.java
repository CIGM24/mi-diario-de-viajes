package com.midiariodeviajes.backend.service;

import com.midiariodeviajes.backend.model.EstadoViaje;
import com.midiariodeviajes.backend.model.Viaje;
import com.midiariodeviajes.backend.repository.ViajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {

    private final ViajeRepository viajeRepository;

    public ViajeService(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
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

    public void eliminar(String id, String usuarioId) {
        Optional<Viaje> viaje = obtenerPorIdYUsuario(id, usuarioId);

        if (viaje.isPresent()) {
            viajeRepository.delete(viaje.get());
        }
    }
}
