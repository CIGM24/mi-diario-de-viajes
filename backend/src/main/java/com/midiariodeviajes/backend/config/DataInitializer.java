package com.midiariodeviajes.backend.config;

import com.midiariodeviajes.backend.model.EstadoViaje;
import com.midiariodeviajes.backend.model.Lugar;
import com.midiariodeviajes.backend.model.Usuario;
import com.midiariodeviajes.backend.model.Viaje;
import com.midiariodeviajes.backend.repository.LugarRepository;
import com.midiariodeviajes.backend.repository.UsuarioRepository;
import com.midiariodeviajes.backend.repository.ViajeRepository;
import com.midiariodeviajes.backend.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(
        prefix = "app.initializer",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ViajeRepository viajeRepository;
    private final LugarRepository lugarRepository;
    private final String demoEmail;
    private final String demoPassword;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService,
            ViajeRepository viajeRepository,
            LugarRepository lugarRepository,
            @Value("${app.initializer.demo-email}") String demoEmail,
            @Value("${app.initializer.demo-password}") String demoPassword
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.viajeRepository = viajeRepository;
        this.lugarRepository = lugarRepository;
        this.demoEmail = demoEmail;
        this.demoPassword = demoPassword;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail(demoEmail).isPresent()) {
            LOGGER.info("Los datos de demostración ya existen; no se han duplicado.");
            return;
        }

        Usuario usuarioDemo = usuarioService.guardar(
                new Usuario("Usuario de demostración", demoEmail, demoPassword)
        );

        LocalDate hoy = LocalDate.now();
        Viaje granada = viajeRepository.save(new Viaje(
                usuarioDemo.getId(),
                "Escapada a Granada",
                "Granada",
                hoy.plusDays(30),
                hoy.plusDays(33),
                450.0,
                "Visita a la Alhambra y al centro histórico.",
                EstadoViaje.PENDIENTE
        ));

        Viaje lisboa = viajeRepository.save(new Viaje(
                usuarioDemo.getId(),
                "Viaje a Lisboa",
                "Lisboa",
                hoy.plusDays(60),
                hoy.plusDays(64),
                700.0,
                "Recorrido por los barrios y miradores de Lisboa.",
                EstadoViaje.PENDIENTE
        ));

        lugarRepository.save(new Lugar(
                granada.getId(),
                "Alhambra",
                "Visita al conjunto monumental.",
                "Calle Real de la Alhambra, Granada",
                37.1761,
                -3.5881
        ));
        lugarRepository.save(new Lugar(
                granada.getId(),
                "Mirador de San Nicolás",
                "Vistas del barrio del Albaicín y la Alhambra.",
                "Plaza de San Nicolás, Granada",
                37.1814,
                -3.5925
        ));
        lugarRepository.save(new Lugar(
                lisboa.getId(),
                "Torre de Belém",
                "Monumento histórico junto al río Tajo.",
                "Av. Brasília, Lisboa",
                38.6916,
                -9.2160
        ));

        LOGGER.info("Datos de demostración creados para la cuenta {}.", demoEmail);
    }
}
