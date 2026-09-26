# AGENTS.md — Mi Diario de Viajes

## 1. Contexto del proyecto

"Mi Diario de Viajes" es una aplicación móvil Android desarrollada como proyecto intermodular del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).

La aplicación permitirá a los usuarios registrar, consultar, modificar y eliminar sus viajes personales, así como gestionar lugares asociados a cada viaje.

El objetivo es desarrollar una aplicación funcional, clara, presentable y técnicamente coherente, manteniendo un alcance realista para el tiempo disponible.

La prioridad es completar un MVP funcional antes de implementar funcionalidades adicionales.

---

## 2. Arquitectura general

La aplicación seguirá inicialmente esta arquitectura:

ANDROID
Kotlin + Jetpack Compose + MVVM
        ↓
Repository
        ↓
Retrofit
        ↓ HTTP/JSON
API REST
        ↓
Spring Boot
        ↓
MongoDB


### Cliente Android

* Kotlin.
* Jetpack Compose.
* Arquitectura MVVM.
* Retrofit para la comunicación con la API REST.
* Repository para centralizar el acceso a datos.

### Backend

* Java.
* Spring Boot.
* API REST.
* Arquitectura basada en Controller → Service → Repository → MongoDB.
* Autenticación mediante JWT.

### Base de datos

* MongoDB.

Las decisiones arquitectónicas podrán ajustarse durante el desarrollo si existe una razón técnica o académica justificada. Los cambios importantes deben evaluarse antes de implementarse.

---

## 3. Estructura general del repositorio

El repositorio utiliza un único repositorio Git para todo el proyecto:

mi-diario-de-viajes/
├── android/
├── backend/
├── README.md
└── AGENTS.md

El directorio `.git` se encuentra en la raíz del proyecto.

Android Studio e IntelliJ trabajan sobre el mismo repositorio Git.

No crear un segundo repositorio Git para `backend`.

La estructura interna de `android` y `backend` podrá evolucionar durante el desarrollo, siempre que se mantenga una organización clara y coherente.

---

## 4. Organización del cliente Android

El proyecto Android utilizará una separación basada en responsabilidades.

### Capa de datos

Debe encargarse de:

* Modelos de datos.
* Comunicación con la API REST.
* Retrofit.
* Repositories.
* Gestión de resultados y errores procedentes del backend.

### Interfaz de usuario

Debe encargarse de:

* Pantallas.
* Componentes reutilizables.
* Navegación.
* Tema visual.
* Estados de carga.
* Estados vacíos.
* Mensajes de error.
* Formularios y validaciones de interfaz.

### ViewModels

Los ViewModels deben encargarse de:

* Gestionar el estado de las pantallas.
* Coordinar las operaciones necesarias.
* Comunicarse con la capa de datos.
* Evitar que las pantallas realicen directamente llamadas HTTP.

Las pantallas no deben acceder directamente a Retrofit.

---

## 5. Organización del backend

El backend debe separar, como mínimo, las siguientes responsabilidades:

### Controller

Gestiona las peticiones HTTP y expone la API REST.

### Service

Contiene la lógica de negocio de la aplicación.

### Repository

Se encarga de la comunicación con MongoDB mediante Spring Data MongoDB.

### Model

Representa las entidades y datos utilizados por la aplicación.

### Seguridad

La autenticación se realizará mediante JWT.

El backend será responsable de:

* Validar el JWT.
* Identificar al usuario autenticado.
* Controlar el acceso a los recursos.
* Impedir que un usuario acceda a viajes o lugares pertenecientes a otro usuario.

No añadir capas adicionales salvo que exista una necesidad real.

---

## 6. Modelo de datos

El modelo principal del proyecto está formado por tres entidades:

Usuario
   │
   │ 1:N
   ▼
Viaje
   │
   │ 1:N
   ▼
Lugar

### Usuario

Campos principales:

Usuario
├── id
├── nombre
├── email
└── password

El email debe ser único.

La contraseña nunca debe almacenarse en texto plano. Debe almacenarse mediante un hash seguro.

### Viaje

Campos principales:

Viaje
├── id
├── usuarioId
├── nombre
├── destino
├── fechaInicio
├── fechaFin
├── presupuesto
├── descripcion
└── estado

El estado inicial utilizará:

PENDIENTE
REALIZADO

### Lugar

Campos principales:

Lugar
├── id
├── viajeId
├── nombre
├── descripcion
├── direccion
├── latitud
└── longitud

Las coordenadas se incluyen para dejar preparado el modelo para una futura integración con Google Maps.

No crear inicialmente una entidad independiente para fotografías.

---

## 7. Colecciones MongoDB

La base de datos utilizará inicialmente las siguientes colecciones:

mi_diario_viajes
├── usuarios
├── viajes
└── lugares

Los lugares se almacenarán como documentos independientes relacionados con un viaje mediante `viajeId`.

Los viajes se relacionarán con el usuario mediante `usuarioId`.

Al eliminar un viaje, deberán eliminarse también sus lugares asociados para evitar datos huérfanos.

---

## 8. Funcionalidad mínima obligatoria

El MVP debe incluir las siguientes funcionalidades.

### Autenticación

* Registro de usuario.
* Login.
* Generación de JWT.
* Validación del JWT.
* Identificación del usuario autenticado.
* Logout mediante eliminación del token almacenado en Android.
* Separación de datos entre usuarios.

### Gestión de viajes

* Crear viajes.
* Consultar viajes.
* Consultar el detalle de un viaje.
* Modificar viajes.
* Eliminar viajes.
* Filtrar viajes por estado.

Cada viaje tendrá inicialmente:

* Nombre.
* Destino.
* Fecha de inicio.
* Fecha de fin.
* Presupuesto.
* Descripción.
* Estado.

### Gestión de lugares

* Añadir lugares a un viaje.
* Consultar lugares de un viaje.
* Modificar lugares.
* Eliminar lugares.

Cada lugar podrá contener:

* Nombre.
* Descripción.
* Dirección.
* Latitud.
* Longitud.

### Persistencia

Los datos deben almacenarse mediante:

Spring Boot → MongoDB

Los datos deben mantenerse aunque la aplicación se cierre.

---

## 9. Seguridad y propiedad de los datos

La seguridad y separación de datos entre usuarios es una parte obligatoria del proyecto.

El cliente Android no debe decidir libremente qué usuario es propietario de un viaje.

Al crear un viaje:

Android
   ↓
JWT
   ↓
Backend identifica al usuario
   ↓
Backend asigna usuarioId

Android no debe enviar un `usuarioId` confiando en que ese valor sea correcto.

Al consultar, modificar o eliminar un viaje, el backend debe comprobar que el viaje pertenece al usuario autenticado.

Para los lugares, el backend debe comprobar:

Lugar
  ↓
Viaje
  ↓
Usuario autenticado

Un usuario nunca debe poder consultar, modificar o eliminar recursos pertenecientes a otro usuario.

La seguridad debe comprobarse mediante pruebas con al menos dos usuarios diferentes.

---

## 10. API REST

La API se organizará inicialmente en los siguientes bloques:

/api/auth
/api/viajes
/api/lugares

### Autenticación

POST /api/auth/register
POST /api/auth/login

El logout se gestionará inicialmente en Android eliminando el JWT almacenado. No es necesario implementar un endpoint de logout en el backend en la primera versión.

### Viajes

GET    /api/viajes
GET    /api/viajes/{id}
POST   /api/viajes
PUT    /api/viajes/{id}
DELETE /api/viajes/{id}

El listado permitirá filtrar por estado:

GET /api/viajes?estado=PENDIENTE

### Lugares

GET    /api/viajes/{viajeId}/lugares
POST   /api/viajes/{viajeId}/lugares
PUT    /api/lugares/{id}
DELETE /api/lugares/{id}

No crear endpoints adicionales hasta que exista una necesidad real.

---

## 11. Códigos HTTP principales

La API utilizará inicialmente los siguientes códigos:

200 OK
201 Created
204 No Content
400 Bad Request
401 Unauthorized
404 Not Found
409 Conflict
500 Internal Server Error

Los errores deben gestionarse de forma controlada y comprensible tanto en backend como en Android.

---

## 12. Pantallas principales Android

La aplicación tendrá inicialmente un número reducido de pantallas.

### Autenticación

* Registro.
* Login.

### Aplicación

* Inicio.
* Mis viajes.
* Detalle de viaje.
* Crear/editar viaje.
* Lugares de un viaje.
* Crear/editar lugar.
* Ajustes, si el tiempo lo permite.

La pantalla de crear y editar debe intentar reutilizar la misma estructura y componentes siempre que sea razonable.

---

## 13. Flujo de datos Android

El flujo principal deberá seguir:

Pantalla
   ↓
ViewModel
   ↓
Repository
   ↓
Retrofit
   ↓
Spring Boot
   ↓
MongoDB

Las pantallas no deben realizar directamente llamadas HTTP.

El ViewModel debe gestionar el estado de la interfaz y coordinar las operaciones necesarias.

---

## 14. Reutilización de código

Evitar código duplicado.

Siempre que sea razonable, reutilizar:

* Componentes de interfaz.
* Formularios.
* Tarjetas de viajes.
* Indicadores de estado.
* Botones.
* Campos de texto.
* Estados de carga.
* Estados vacíos.
* Mensajes de error.
* Diálogos de confirmación.
* Lógica común.

La reutilización no debe llevar a crear abstracciones innecesariamente complejas.

Priorizar código sencillo y fácil de comprender.

---

## 15. Orden de desarrollo

El proyecto seguirá esta prioridad:

1. Backend + MongoDB
2. Autenticación + JWT
3. CRUD de viajes
4. CRUD de lugares
5. Android conectado al backend
6. Validaciones y gestión de errores
7. Pulido visual
8. Google Maps
9. Fotografías

No comenzar una prioridad inferior mientras una prioridad superior esté rota o incompleta de forma que impida continuar.

El orden podrá modificarse únicamente cuando exista una razón técnica o académica clara.

---

## 16. Desarrollo incremental

El proyecto debe desarrollarse de forma incremental.

Cada bloque importante debe seguir, siempre que sea posible:

1. Diseñar
2. Implementar
3. Ejecutar
4. Comprobar
5. Corregir
6. Hacer commit
7. Hacer push
8. Continuar

No realizar grandes cantidades de cambios simultáneamente sin comprobar el funcionamiento.

Cuando una parte que ya funciona no necesita modificarse, evitar tocarla innecesariamente.

---

## 17. Reglas de Git

El proyecto utiliza un único repositorio Git.

Los commits deben ser pequeños y representar cambios coherentes.

Ejemplos:

Configuración inicial del backend
Añadido modelo Usuario
Añadido modelo Viaje y estado del viaje
Añadido modelo Lugar
Añadido repositorio de Usuario
Implementado registro de usuarios
Implementado login con JWT

Antes de hacer commit:

* Comprobar que el código compila.
* Comprobar que la aplicación afectada arranca correctamente.
* Revisar los archivos modificados.

No crear un nuevo repositorio Git para una parte del proyecto.

---

## 18. Principios de desarrollo

Priorizar:

1. Funcionalidad.
2. Simplicidad.
3. Código mantenible.
4. Claridad.
5. Estabilidad.
6. Seguridad.
7. Reutilización.
8. Presentación visual.

Evitar introducir tecnologías o patrones innecesarios.

No utilizar inicialmente, salvo que posteriormente exista una necesidad justificada:

* Clean Architecture.
* Hilt/Dagger.
* Firebase.
* Room.
* Microservicios.
* Docker.
* Servicios cloud.
* Librerías innecesarias.

La ausencia de estas tecnologías no debe considerarse un problema si no son necesarias para los objetivos del proyecto.

---

## 19. Funcionalidades de ampliación

Las funcionalidades de ampliación solo deben implementarse después de completar y probar el MVP.

### Google Maps

Se podrá integrar Google Maps para:

* Mostrar la ubicación de lugares.
* Utilizar las coordenadas almacenadas.
* Facilitar la consulta de lugares.

Google Maps no es necesario para que el MVP se considere terminado.

### Fotografías

Se podrá estudiar posteriormente la posibilidad de:

* Seleccionar fotografías.
* Asociarlas a lugares.
* Gestionar archivos.

La gestión de fotografías es una ampliación avanzada y no debe poner en riesgo el MVP.

---

## 20. Funcionalidades fuera de alcance

No implementar inicialmente:

* Reservas de vuelos.
* Reservas de hoteles.
* Gestión detallada de gastos.
* Compartir viajes.
* Sistema social.
* Comentarios.
* Chat.
* Notificaciones.
* Recomendaciones automáticas.
* Sincronización offline avanzada.
* Múltiples servicios externos.
* Funcionalidades que no aporten directamente al objetivo del proyecto.

Estas funcionalidades solo podrían reconsiderarse si las funcionalidades principales están completamente terminadas y existe tiempo suficiente.

---

## 21. Uso de IA

La IA se utilizará como herramienta de apoyo al desarrollo.

Puede utilizarse para:

* Explicar conceptos.
* Analizar errores.
* Proponer soluciones.
* Comparar alternativas.
* Revisar código.
* Generar código supervisado.
* Mejorar código existente.
* Crear documentación.
* Proponer pruebas.

Todo código generado o modificado mediante IA debe ser:

1. Revisado.
2. Comprendido.
3. Ejecutado.
4. Probado antes de incorporarlo al proyecto.

La IA debe respetar la arquitectura, los objetivos y el alcance definidos en este archivo.

No introducir nuevas tecnologías, librerías o patrones arquitectónicos importantes sin explicar previamente su necesidad y valorar su impacto.

La IA debe trabajar de forma incremental y no realizar cambios innecesarios.

---

## 22. Forma de trabajar con IA

La IA debe seguir estas reglas durante el desarrollo:

* Trabajar paso a paso.
* No adelantarse innecesariamente a fases posteriores.
* Explicar los cambios importantes antes de realizarlos.
* No modificar configuraciones que ya funcionan sin una razón clara.
* No añadir funcionalidades "porque ya que estamos aquí".
* Mantener el proyecto dentro del alcance definido.
* Priorizar un MVP funcional.
* Comprobar los cambios antes de continuar.
* Mantener commits pequeños y coherentes.
* Consultar antes de introducir una modificación importante de arquitectura.
* Si existe una solución sencilla y otra excesivamente compleja, priorizar inicialmente la solución sencilla.
* No asumir que una funcionalidad debe implementarse simplemente porque sea técnicamente posible.

Si una tarea no está contemplada en el alcance o en la hoja de ruta, primero debe decidirse dónde encaja antes de implementarla.

---

## 23. Criterio para tomar decisiones

Cuando existan varias soluciones posibles, priorizar la solución que:

* Sea sencilla de implementar.
* Requiera poco código duplicado.
* Sea fácil de entender y mantener.
* Encaje con la arquitectura definida.
* Sea adecuada para un proyecto académico de DAM.
* Sea segura.
* Permita terminar el proyecto dentro del tiempo disponible.

Evitar soluciones excesivamente sofisticadas para problemas sencillos.

No introducir una tecnología únicamente porque sea más moderna o avanzada.

La solución debe aportar un beneficio real al proyecto.

---

## 24. Criterio de proyecto terminado

El MVP se considerará funcionalmente terminado cuando sea posible:

1. Registrar un usuario.
2. Iniciar sesión.
3. Obtener y utilizar un JWT.
4. Crear un viaje.
5. Guardarlo en MongoDB.
6. Cerrar la aplicación.
7. Volver a iniciar sesión.
8. Consultar el viaje guardado.
9. Modificar el viaje.
10. Añadir lugares al viaje.
11. Consultar los lugares.
12. Modificar un lugar.
13. Eliminar un lugar.
14. Filtrar viajes por estado.
15. Eliminar un viaje.
16. Comprobar que al eliminar un viaje se eliminan sus lugares asociados.
17. Comprobar que un usuario no puede acceder a los viajes de otro usuario.

Después de completar este punto se podrán abordar mejoras visuales y funcionalidades de ampliación.

---

## 25. Alcance del proyecto

El proyecto tiene un tiempo limitado de desarrollo.

La prioridad es terminar correctamente un MVP funcional antes de añadir características adicionales.

Ante una elección entre:

* una solución sencilla que funciona, y
* una solución más compleja con características adicionales,

se priorizará inicialmente la solución sencilla y funcional.

Las mejoras visuales y funcionalidades adicionales se realizarán después de completar el núcleo de la aplicación.

El objetivo no es implementar el mayor número posible de funcionalidades, sino conseguir una aplicación completa, funcional, estable y presentable dentro del tiempo disponible.
