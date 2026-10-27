package com.midiariodeviajes.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;

public class LugarRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;
    //viajeId seguirá controlado por el backend mediante la URL.
    @Size(max = 500)
    private String descripcion;

    @Size(max = 200)
    private String direccion;

    private Double latitud;

    private Double longitud;

    public LugarRequest() {
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

    @AssertTrue(message = "La latitud debe estar entre -90 y 90")
    public boolean isLatitudEnRango() {
        return latitud == null || (latitud >= -90.0 && latitud <= 90.0);
    }

    @AssertTrue(message = "La longitud debe estar entre -180 y 180")
    public boolean isLongitudEnRango() {
        return longitud == null || (longitud >= -180.0 && longitud <= 180.0);
    }

    @AssertTrue(message = "Debes indicar tanto la latitud como la longitud")
    public boolean isParDeCoordenadasValido() {
        return (latitud == null) == (longitud == null);
    }
}