package com.midiariodeviajes.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class ConfiguracionFotos implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/viajes}")
    private String directorioFotos;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path ruta = Paths.get(directorioFotos)
                .toAbsolutePath()
                .normalize();

        registry.addResourceHandler("/fotos/**")
                .addResourceLocations(ruta.toUri().toString());
    }
}
