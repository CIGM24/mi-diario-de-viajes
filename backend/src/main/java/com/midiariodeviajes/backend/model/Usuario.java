package com.midiariodeviajes.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;


    @Document(collection = "usuarios")
    public class Usuario {

        @Id
        private String id;

        private String nombre;

        @Indexed(unique = true)
        private String email;

        private String password;

        public Usuario() {
        }

        public Usuario(String nombre, String email, String password) {
            this.nombre = nombre;
            this.email = email;
            this.password = password;
        }

        public String getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

