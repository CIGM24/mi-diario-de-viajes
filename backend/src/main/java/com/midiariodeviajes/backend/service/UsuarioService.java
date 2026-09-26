package com.midiariodeviajes.backend.service;

import com.midiariodeviajes.backend.model.Usuario;
import com.midiariodeviajes.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }
}
