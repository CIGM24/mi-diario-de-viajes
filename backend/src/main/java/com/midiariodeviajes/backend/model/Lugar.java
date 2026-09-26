package com.midiariodeviajes.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "lugares")
public class Lugar {

    @Id
    private String id;

    private String viajeId;

    private String nombre;

    private String descripcion;

    private String direccion;

    private Double latitud;

    private Double longitud;

    public Lugar() {
    }

    public Lugar(String viajeId, String nombre, String descripcion,
                 String direccion, Double latitud, Double longitud) {
        this.viajeId = viajeId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
