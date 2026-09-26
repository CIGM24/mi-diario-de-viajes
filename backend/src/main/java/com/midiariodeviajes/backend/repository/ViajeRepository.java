package com.midiariodeviajes.backend.repository;

import com.midiariodeviajes.backend.model.EstadoViaje;
import com.midiariodeviajes.backend.model.Viaje;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ViajeRepository extends MongoRepository<Viaje, String> {

    List<Viaje> findByUsuarioId(String usuarioId);

    List<Viaje> findByUsuarioIdAndEstado(String usuarioId, EstadoViaje estado);
}
