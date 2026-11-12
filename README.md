# ✈️ Mi Diario de Viajes

Aplicación Android para organizar viajes, gestionar lugares de interés, guardar fotografías y consultar ubicaciones en Google Maps.

## Funcionalidades

- Registro e inicio de sesión con autenticación JWT.
- Crear, consultar, editar y eliminar viajes.
- Filtrar viajes por estado.
- Añadir, consultar, editar y eliminar lugares asociados a cada viaje.
- Abrir lugares en Google Maps mediante coordenadas o dirección.
- Añadir una fotografía de portada a cada viaje.
- Mantener los datos separados por usuario.

## Tecnologías

- **Aplicación Android:** Kotlin, Jetpack Compose, Retrofit y Coil.
- **Backend:** Java, Spring Boot y API REST.
- **Base de datos:** MongoDB.
- **Autenticación:** JWT.

## Arquitectura

```text
Android (Kotlin · Jetpack Compose · MVVM)
                  ↓ API REST · JSON
Backend (Java · Spring Boot · JWT)
                  ↓
               MongoDB