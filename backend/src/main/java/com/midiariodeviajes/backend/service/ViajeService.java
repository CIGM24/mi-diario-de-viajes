package com.midiariodeviajes.backend.service;

import com.midiariodeviajes.backend.model.EstadoViaje;
import com.midiariodeviajes.backend.model.Viaje;
import com.midiariodeviajes.backend.repository.LugarRepository;
import com.midiariodeviajes.backend.repository.ViajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {

    private final ViajeRepository viajeRepository;
    private final LugarRepository lugarRepository;

    public ViajeService(ViajeRepository viajeRepository,
                        LugarRepository lugarRepository) {
        this.viajeRepository = viajeRepository;
        this.lugarRepository = lugarRepository;
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

        lugarRepository.deleteAll(
                lugarRepository.findByViajeId(id)
        );

        viajeRepository.delete(viaje.get());

        return true;
    }
}
