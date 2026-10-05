package com.midiariodeviajes.backend.service;

import com.midiariodeviajes.backend.model.Lugar;
import com.midiariodeviajes.backend.repository.LugarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LugarService {

    private final LugarRepository lugarRepository;
    private final ViajeService viajeService;

    public LugarService(
            LugarRepository lugarRepository,
            ViajeService viajeService) {

        this.lugarRepository = lugarRepository;
        this.viajeService = viajeService;
    }

    public List<Lugar> obtenerPorViaje(
            String viajeId,
            String usuarioId) {

        if (viajeService.obtenerPorIdYUsuario(viajeId, usuarioId).isEmpty()) {
            return List.of();
        }

        return lugarRepository.findByViajeId(viajeId);
    }

    public Optional<Lugar> obtenerPorId(
            String id,
            String usuarioId) {

        return lugarRepository.findById(id)
                .filter(lugar ->
                        viajeService.obtenerPorIdYUsuario(
                                lugar.getViajeId(),
                                usuarioId
                        ).isPresent()
                );
    }

    public Optional<Lugar> guardar(
            Lugar lugar,
            String usuarioId) {

        if (viajeService.obtenerPorIdYUsuario(
                lugar.getViajeId(),
                usuarioId
        ).isEmpty()) {

            return Optional.empty();
        }

        return Optional.of(lugarRepository.save(lugar));
    }

    public void eliminar(
            String id,
            String usuarioId) {

        obtenerPorId(id, usuarioId)
                .ifPresent(lugarRepository::delete);
    }
}

/*package com.midiariodeviajes.backend.service;

import com.midiariodeviajes.backend.model.Lugar;
import com.midiariodeviajes.backend.repository.LugarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LugarService {

    private final LugarRepository lugarRepository;

    public LugarService(LugarRepository lugarRepository) {
        this.lugarRepository = lugarRepository;
    }

    public List<Lugar> obtenerPorViaje(String viajeId) {
        return lugarRepository.findByViajeId(viajeId);
    }

    public Optional<Lugar> obtenerPorId(String id) {
        return lugarRepository.findById(id);
    }

    public Lugar guardar(Lugar lugar) {
        return lugarRepository.save(lugar);
    }

    public void eliminar(String id) {
        lugarRepository.deleteById(id); //Hay que cambiarlo para asegurarnos de que un
        // usuario no pueda eliminar un lugar perteneciente a otro usuario
    }
}

 */
