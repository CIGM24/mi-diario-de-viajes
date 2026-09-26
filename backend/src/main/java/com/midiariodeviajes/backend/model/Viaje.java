package com.midiariodeviajes.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

    @Document(collection = "viajes")
    public class Viaje {

        @Id
        private String id;

        private String usuarioId;

        private String nombre;

        private String destino;

        private LocalDate fechaInicio;

        private LocalDate fechaFin;

        private Double presupuesto;

        private String descripcion;

        private EstadoViaje estado;

        public Viaje() {
        }

        public Viaje(String usuarioId, String nombre, String destino,
                     LocalDate fechaInicio, LocalDate fechaFin,
                     Double presupuesto, String descripcion,
                     EstadoViaje estado) {
            this.usuarioId = usuarioId;
            this.nombre = nombre;
            this.destino = destino;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.presupuesto = presupuesto;
            this.descripcion = descripcion;
            this.estado = estado;
        }

        public String getId() {
            return id;
        }

        public String getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(String usuarioId) {
            this.usuarioId = usuarioId;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDestino() {
            return destino;
        }

        public void setDestino(String destino) {
            this.destino = destino;
        }

        public LocalDate getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(LocalDate fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public LocalDate getFechaFin() {
            return fechaFin;
        }

        public void setFechaFin(LocalDate fechaFin) {
            this.fechaFin = fechaFin;
        }

        public Double getPresupuesto() {
            return presupuesto;
        }

        public void setPresupuesto(Double presupuesto) {
            this.presupuesto = presupuesto;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public EstadoViaje getEstado() {
            return estado;
        }

        public void setEstado(EstadoViaje estado) {
            this.estado = estado;
        }
    }

