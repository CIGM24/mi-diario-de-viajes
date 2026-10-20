package com.midiariodeviajes.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class AlmacenamientoFotosService {

    private static final Set<String> TIPOS_PERMITIDOS =
            Set.of("image/jpeg", "image/png", "image/webp");

    private static final long TAMANO_MAXIMO = 5 * 1024 * 1024;

    private final Path directorioFotos;

    public AlmacenamientoFotosService(
            @Value("${app.upload.dir:uploads/viajes}") String directorio) {

        this.directorioFotos = Paths.get(directorio)
                .toAbsolutePath()
                .normalize();
    }

    public String guardar(MultipartFile archivo) throws IOException {

        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar una fotografía.");
        }

        if (archivo.getSize() > TAMANO_MAXIMO) {
            throw new IllegalArgumentException(
                    "La fotografía no puede superar los 5 MB.");
        }

        String tipo = archivo.getContentType();

        if (tipo == null || !TIPOS_PERMITIDOS.contains(tipo.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Solo se permiten imágenes JPG, PNG o WEBP.");
        }

        String extension;

        switch (tipo.toLowerCase()) {
            case "image/jpeg":
                extension = ".jpg";
                break;
            case "image/png":
                extension = ".png";
                break;
            case "image/webp":
                extension = ".webp";
                break;
            default:
                throw new IllegalArgumentException("Formato de imagen no permitido.");
        }

        Files.createDirectories(directorioFotos);

        String nombreArchivo = UUID.randomUUID() + extension;
        Path destino = directorioFotos.resolve(nombreArchivo).normalize();

        if (!destino.startsWith(directorioFotos)) {
            throw new IllegalArgumentException("Ruta de archivo no válida.");
        }

        try (InputStream entrada = archivo.getInputStream()) {
            Files.copy(entrada, destino, StandardCopyOption.REPLACE_EXISTING);
        }

        return nombreArchivo;
    }

    public Resource cargar(String nombreArchivo) throws IOException {
        Path archivo = directorioFotos.resolve(nombreArchivo).normalize();

        if (!archivo.startsWith(directorioFotos)
                || !directorioFotos.equals(archivo.getParent())) {
            throw new IllegalArgumentException("Nombre de archivo no válido.");
        }

        if (!Files.isRegularFile(archivo) || !Files.isReadable(archivo)) {
            throw new IOException("No se encontró la fotografía.");
        }

        return new UrlResource(archivo.toUri());
    }
}

