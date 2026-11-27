# ✈️ Mi Diario de Viajes

Aplicación Android para organizar viajes, guardar lugares de interés y añadir una fotografía de portada a cada viaje. Los datos se gestionan mediante una API REST protegida con JWT y se almacenan en MongoDB.

## Funcionalidades

- Registro e inicio de sesión con autenticación JWT.
- Creación, consulta, edición y eliminación de viajes.
- Filtros por estado del viaje.
- Gestión de lugares asociados a cada viaje.
- Apertura de ubicaciones en Google Maps.
- Subida, visualización y sustitución de fotografías de portada.
- Separación de los datos por usuario.

## Tecnologías

- **Android:** Kotlin, Jetpack Compose, MVVM, Retrofit y Coil.
- **Backend:** Java 17, Spring Boot y API REST.
- **Base de datos:** MongoDB.
- **Autenticación:** Spring Security y JWT.

## Requisitos previos

- Git.
- JDK 17 para el backend.
- MongoDB local o una instancia accesible.
- Android Studio con Android SDK Platform 34.
- Un emulador Android o un dispositivo Android conectado a la misma red que el ordenador.

El repositorio incluye los wrappers de Maven y Gradle.

## Instalación y ejecución local

### 1. Clonar el repositorio

Clona el repositorio y abre su carpeta raíz en Android Studio.

### 2. Configurar el backend

Inicia MongoDB. La base `mi_diario_viajes` y sus colecciones se crean al guardar los primeros documentos.

Desde la raíz del repositorio, copia la plantilla de configuración:

```powershell
Copy-Item backend\src\main\resources\application.properties.example backend\src\main\resources\application.properties
```

`application.properties` está excluido de Git. La plantilla contiene valores locales para MongoDB y la carpeta de fotos; el secreto JWT se proporciona mediante la variable `JWT_SECRET`. En PowerShell, define un secreto aleatorio de al menos 32 caracteres antes de arrancar el backend:

```powershell
$env:JWT_SECRET = "SUSTITUIR_POR_UN_SECRETO_ALEATORIO_DE_32_CARACTERES_O_MAS"
```

Si MongoDB no está en `localhost:27017`, configura también `SPRING_MONGODB_URI`. Para cambiar la base de datos o la ruta de fotos, utiliza `SPRING_MONGODB_DATABASE` y `APP_UPLOAD_DIR`.

Desde la raíz del repositorio, inicia el backend:

```powershell
Set-Location backend
.\mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080/`. La carpeta de fotos local predeterminada es `backend/uploads/viajes`; su contenido no se sube a Git.

### 3. Ejecutar Android

En Android Studio, sincroniza Gradle y ejecuta la aplicación en un emulador o dispositivo.

Antes de iniciar la app, revisa `app/src/main/java/com/example/midiariodeviajes/data/ApiConfig.kt` y configura la URL del backend:

- **Emulador Android:** `http://10.0.2.2:8080/`.
- **Dispositivo físico:** `http://IP_LOCAL_DEL_ORDENADOR:8080/`. Ambos dispositivos deben estar en la misma red.

La URL debe terminar en `/`. En un entorno desplegado, se debe usar la dirección HTTPS pública del backend.

## Datos y credenciales de demostración

Al arrancar el backend, `DataInitializer` crea una cuenta de demostración con viajes y lugares de ejemplo si está habilitado y esa cuenta aún no existe en la base de datos. Los valores predeterminados del correo y la contraseña están configurados en `application.yml`; se pueden cambiar con `DEMO_USER_EMAIL` y `DEMO_USER_PASSWORD`. Facilita al evaluador las credenciales que hayas configurado.

En una base de datos nueva, las credenciales predeterminadas son:

- **Correo:** `demo@example.com`
- **Contraseña:** `ViajesDemo123!`

Si se sustituyen mediante variables de entorno, utiliza esos valores configurados. El inicializador no cambia la contraseña de una cuenta demo que ya exista.

El inicializador no crea fotografías. Se pueden añadir desde la aplicación. Para desactivar los datos de demostración en una base compartida o con datos reales, configura `APP_INITIALIZER_ENABLED=false`.

## Variables de entorno del backend

| Variable | Uso | Valor local predeterminado |
|---|---|---|
| `SPRING_MONGODB_URI` | URI de conexión a MongoDB | `mongodb://localhost:27017` |
| `SPRING_MONGODB_DATABASE` | Nombre de la base de datos | `mi_diario_viajes` |
| `JWT_SECRET` | Secreto para firmar los tokens JWT | Obligatorio; definir uno aleatorio y privado |
| `APP_UPLOAD_DIR` | Carpeta de almacenamiento de fotografías | `uploads/viajes` |
| `APP_INITIALIZER_ENABLED` | Activa o desactiva los datos de demostración | `true` |
| `DEMO_USER_EMAIL` | Correo de la cuenta de demostración | Configurado en `application.yml` |
| `DEMO_USER_PASSWORD` | Contraseña de la cuenta de demostración | Configurada en `application.yml` |

No subas a Git secretos reales, credenciales personales, `application.properties` ni fotografías cargadas por usuarios.

## Despliegue

El backend requiere una instancia accesible de MongoDB y las variables de entorno anteriores. En el proveedor elegido, las fotografías deben guardarse en almacenamiento persistente; el sistema de archivos temporal de un servicio puede perderlas al reiniciar o desplegar. La app Android debe configurarse con la URL HTTPS pública del backend.
