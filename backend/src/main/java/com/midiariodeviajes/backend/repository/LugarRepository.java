package com.midiariodeviajes.backend.repository;

import com.midiariodeviajes.backend.model.Lugar;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LugarRepository extends MongoRepository<Lugar, String> {

    List<Lugar> findByViajeId(String viajeId);
}
