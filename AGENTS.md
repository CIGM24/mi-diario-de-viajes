# AGENTS.md — Mi Diario de Viajes

## 1. Contexto del proyecto

"Mi Diario de Viajes" es una aplicación móvil Android desarrollada como proyecto intermodular del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).

La aplicación permitirá registrar, consultar, modificar y eliminar viajes personales, almacenando información básica como destino, fechas, presupuesto, descripción y estado.

El objetivo es desarrollar una aplicación funcional, clara y presentable, manteniendo un alcance realista para el tiempo disponible.

---

## 2. Arquitectura general

La aplicación seguirá inicialmente esta arquitectura:

```text
Android
(Kotlin + Jetpack Compose)
        ↓
Retrofit
        ↓
API REST
        ↓
Spring Boot
        ↓
MongoDB
```

### Cliente Android

* Kotlin.
* Jetpack Compose.
* Arquitectura MVVM.
* Retrofit para la comunicación con la API REST.

### Backend

* Java.
* Spring Boot.
* API REST.

### Base de datos

* MongoDB.

Las tecnologías y decisiones arquitectónicas podrán ajustarse durante el desarrollo si existe una razón técnica o académica justificada.

---

## 3. Estructura general del repositorio

El repositorio estará organizado inicialmente de forma similar a:

```text
mi-diario-de-viajes/
├── android/
├── backend/
├── README.md
└── AGENTS.md
```

La estructura interna de `android` y `backend` podrá evolucionar durante el desarrollo.

No es necesario utilizar exactamente unos nombres concretos para paquetes, clases, archivos o componentes mientras se respete la arquitectura general y se mantenga una estructura clara y coherente.

---

## 4. Organización del cliente Android

El proyecto Android utilizará una separación basada en responsabilidades:

### Capa de datos

Debe encargarse de:

* Modelos de datos.
* Comunicación con la API REST.
* Acceso a los datos.
* Repositorios o mecanismos equivalentes para centralizar las operaciones de datos.

### Interfaz de usuario

Debe encargarse de:

* Pantallas.
* Componentes reutilizables.
* Navegación.
* Tema visual.
* Estados de carga, error y ausencia de datos.

### ViewModels

Los ViewModels deben encargarse de:

* Gestionar el estado de las pantallas.
* Coordinar las operaciones necesarias.
* Comunicarse con la capa de datos.
* Evitar que las pantallas realicen directamente las llamadas a la API.

Los nombres concretos de las clases, archivos y ViewModels se decidirán durante la implementación.

---

## 5. Organización del backend

El backend debe separar, como mínimo, las siguientes responsabilidades:

### Controller

Gestiona las peticiones HTTP y expone la API REST.

### Service

Contiene la lógica de negocio de la aplicación.

### Repository

Se encarga de la comunicación con MongoDB.

### Model

Representa las entidades y datos utilizados por la aplicación.

Los nombres concretos de las clases y paquetes se decidirán durante la implementación.

No añadir capas adicionales salvo que exista una necesidad real.

---

## 6. Funcionalidad mínima obligatoria

El proyecto debe centrarse inicialmente en estas funcionalidades:

### Gestión de viajes

* Crear viajes.
* Consultar viajes.
* Modificar viajes.
* Eliminar viajes.

Cada viaje tendrá inicialmente información como:

* Nombre.
* Destino.
* Fecha de inicio.
* Fecha de fin.
* Presupuesto.
* Descripción.
* Estado.

La estructura exacta del modelo podrá ajustarse durante la implementación si fuese necesario.

### Persistencia

Los viajes deben almacenarse mediante:

```text
Spring Boot → MongoDB
```

Los datos deben mantenerse aunque la aplicación se cierre.

### Consulta y organización

La aplicación debe permitir:

* Mostrar los viajes registrados.
* Consultarlos de forma organizada.
* Filtrarlos como mínimo por estado.

Los estados iniciales podrán ser, por ejemplo:

* Pendiente.
* Completado.

---

## 7. Pantallas principales

La aplicación tendrá inicialmente un número reducido de pantallas para mantener el proyecto dentro del alcance previsto:

1. Pantalla principal / listado de viajes.
2. Pantalla para crear o editar un viaje.
3. Pantalla de detalle de un viaje.
4. Pantalla de ajustes o información, si el tiempo lo permite.

La pantalla de crear y editar debe intentar reutilizar la misma estructura y componentes siempre que sea razonable.

Los nombres concretos de las pantallas y archivos se decidirán durante la implementación.

---

## 8. Reutilización de código

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

Cuando dos pantallas tengan funcionalidades similares, valorar primero si pueden compartir componentes, funciones o lógica antes de duplicar código.

La reutilización no debe llevar a crear abstracciones innecesariamente complejas.

---

## 9. Modelo principal

El modelo principal será el concepto de "Viaje".

Inicialmente deberá contemplar:

```text
Viaje
├── identificador
├── nombre
├── destino
├── fecha de inicio
├── fecha de fin
├── presupuesto
├── descripción
└── estado
```

Los nombres concretos de los atributos dependerán de las convenciones utilizadas en cada tecnología.

No añadir campos innecesarios sin una razón clara.

---

## 10. API REST

La API deberá proporcionar inicialmente operaciones equivalentes a:

```text
GET    /api/viajes
GET    /api/viajes/{id}
POST   /api/viajes
PUT    /api/viajes/{id}
DELETE /api/viajes/{id}
```

Estas rutas podrán modificarse durante la implementación si existe una razón técnica justificada.

No crear endpoints adicionales hasta que exista una necesidad real.

---

## 11. Flujo de datos Android

El flujo principal deberá seguir una estructura similar a:

```text
Pantalla
   ↓
ViewModel
   ↓
Capa de datos / Repository
   ↓
Retrofit
   ↓
Spring Boot
   ↓
MongoDB
```

Las pantallas no deben realizar directamente las llamadas HTTP.

Los nombres y clases concretas utilizados para implementar este flujo se decidirán durante el desarrollo.

---

## 12. Principios de desarrollo

Priorizar:

1. Funcionalidad.
2. Simplicidad.
3. Código mantenible.
4. Reutilización.
5. Claridad.
6. Estabilidad.
7. Presentación visual.

Evitar introducir tecnologías o patrones innecesarios.

No utilizar inicialmente, salvo que posteriormente sean necesarios:

* Clean Architecture.
* Hilt/Dagger.
* Firebase.
* Room.
* Microservicios.
* Autenticación/JWT.
* Docker.
* Servicios cloud.

La ausencia de estas tecnologías no debe considerarse un problema si no son necesarias para los objetivos del proyecto.

---

## 13. Funcionalidades opcionales

Solo implementar funcionalidades opcionales cuando el núcleo del proyecto esté terminado.

Posibles funcionalidades opcionales:

* Lugares de interés asociados a un viaje.
* Marcar lugares como visitados o pendientes.
* Mejoras visuales.
* Animaciones sencillas.
* Resúmenes o estadísticas básicas.

Las funcionalidades opcionales nunca deben poner en riesgo las funcionalidades mínimas.

---

## 14. Uso de IA

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

Todo código generado o modificado mediante IA debe ser revisado, comprendido y probado antes de incorporarlo al proyecto.

La IA debe respetar la arquitectura, los objetivos y el alcance definidos en este archivo.

No introducir nuevas tecnologías, librerías o patrones arquitectónicos importantes sin explicar previamente su necesidad y valorar su impacto en el proyecto.

---

## 15. Forma de trabajar

El proyecto debe desarrollarse de forma incremental.

Orden orientativo:

1. Preparar la estructura base.
2. Crear el backend.
3. Configurar MongoDB.
4. Implementar la API REST.
5. Crear las pantallas Android.
6. Implementar MVVM.
7. Conectar Android mediante Retrofit.
8. Implementar el CRUD completo.
9. Implementar el filtrado.
10. Mejorar la interfaz.
11. Realizar pruebas.
12. Completar la documentación.
13. Añadir funcionalidades opcionales si queda tiempo.

Este orden puede modificarse cuando exista una razón técnica o de aprendizaje.

No avanzar a funcionalidades secundarias mientras las funcionalidades principales no sean estables.

---

## 16. Criterio para tomar decisiones

Cuando existan varias soluciones posibles, priorizar la solución que:

* Sea sencilla de implementar.
* Requiera poco código duplicado.
* Sea fácil de entender y mantener.
* Encaje con la arquitectura definida.
* Sea adecuada para un proyecto académico de DAM.
* Permita terminar el proyecto dentro del tiempo disponible.

Evitar soluciones excesivamente sofisticadas para problemas sencillos.

No asumir que una tecnología, clase, paquete, archivo o nombre concreto debe utilizarse si todavía no ha sido decidido.

Antes de introducir una nueva tecnología o una modificación importante de arquitectura, explicar la razón y valorar si aporta un beneficio real al proyecto.

---

## 17. Alcance del proyecto

El proyecto tiene un tiempo limitado de desarrollo.

La prioridad es terminar correctamente un MVP funcional antes de añadir características adicionales.

Ante una elección entre:

* una solución sencilla que funciona, y
* una solución más compleja con características adicionales,

se priorizará inicialmente la solución sencilla y funcional.

Las mejoras visuales y funcionalidades adicionales se realizarán después de completar el núcleo de la aplicación.
