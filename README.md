# CRUD - Javalin - MongoDB

Proyecto **demostrativo** para el curso de Programación Web (PUCMM). Integra
[Javalin](https://javalin.io/) con [MongoDB](https://www.mongodb.com/) e implementa
un CRUD completo de estudiantes de dos formas distintas:

1. **API REST** en JSON (`/api/estudiante`).
2. **CRUD tradicional** renderizado en el servidor con plantillas Thymeleaf
   (`/crud-simple/...`).

Incluye además una página "single page" que consume la API vía JavaScript
(`/crud-single-page.html`).

## Tecnologías utilizadas

| Herramienta        | Versión   |
|--------------------|-----------|
| Java (JDK)         | 25 (LTS)  |
| Gradle             | 9.6.1     |
| Javalin            | 6.7.0     |
| MongoDB (driver)   | 5.5.1     |
| Thymeleaf          | 3.1.3     |
| Jackson            | 2.19.0    |
| DataFaker          | 2.4.3     |
| SLF4J              | 2.0.17    |
| Docker / Compose   | —         |

## Configuración

La aplicación se configura mediante **variables de entorno**:

| Variable     | Descripción                                   | Ejemplo                     |
|--------------|-----------------------------------------------|-----------------------------|
| `URL_MONGO`  | Cadena de conexión a MongoDB.                 | `mongodb://localhost:27017` |
| `DB_NOMBRE`  | Nombre de la base de datos.                   | `pucmm`                     |
| `PORT`       | Puerto del servidor (opcional, por def. 7000).| `7000`                      |

## Cómo ejecutar

### Opción A — Docker Compose (recomendada)

Levanta MongoDB y la aplicación con un solo comando (no requiere tener Java ni
MongoDB instalados, solo Docker):

```bash
docker compose up --build
```

Cuando termine, abre: <http://localhost:7000/>

Para detener y borrar los datos:

```bash
docker compose down -v
```

### Opción B — Gradle (local)

Requiere una instancia de MongoDB accesible. Define las variables de entorno y
ejecuta:

```bash
export URL_MONGO="mongodb://localhost:27017"
export DB_NOMBRE="pucmm"

./gradlew run
```

> Gradle descargará automáticamente el JDK 25 mediante *toolchains* si no lo tienes instalado.

### Generar el JAR ejecutable (fat JAR)

```bash
./gradlew shadowJar
java -jar build/libs/crud-dns.jar
```

## Endpoints principales

| Método | Ruta                              | Descripción                          |
|--------|-----------------------------------|--------------------------------------|
| GET    | `/api/estudiante/`                | Lista todos los estudiantes (JSON).  |
| GET    | `/api/estudiante/{matricula}`     | Obtiene un estudiante por matrícula. |
| POST   | `/api/estudiante/`                | Crea un estudiante (body JSON).      |
| PUT    | `/api/estudiante/`                | Actualiza un estudiante (body JSON). |
| DELETE | `/api/estudiante/{matricula}`     | Elimina un estudiante.               |
| GET    | `/crud-simple/listar`             | CRUD tradicional (HTML/Thymeleaf).   |
| GET    | `/crud-single-page.html`          | SPA que consume la API.              |
| GET    | `/rutas`                          | Listado de todas las rutas (overview).|

### Ejemplo con `curl`

```bash
# Crear
curl -X POST http://localhost:7000/api/estudiante/ \
  -H "Content-Type: application/json" \
  -d '{"matricula":1001,"nombre":"Ana Perez","carrera":"Ingenieria"}'

# Listar
curl http://localhost:7000/api/estudiante/
```

## Estructura del proyecto

```
src/main/java/edu/pucmm/pw/
├── Main.java                       # Configuración y arranque del servidor Javalin
├── controladores/
│   ├── ApiControlador.java         # Rutas de la API REST
│   └── CrudTradicionalControlador.java  # Rutas del CRUD con plantillas
├── entidades/
│   └── Estudiante.java             # POJO / modelo
├── servicios/
│   ├── EstudianteServices.java     # Lógica de negocio (Singleton)
│   └── MongoDbConexion.java        # Conexión a MongoDB (Singleton)
└── util/
    ├── BaseControlador.java        # Clase base de los controladores
    ├── NoExisteEstudianteException.java
    └── TablasMongo.java            # Nombres de las colecciones

src/main/resources/
├── publico/                        # Archivos estáticos (SPA)
└── templates/crud-tradicional/     # Plantillas Thymeleaf
```

## Notas de migración (Javalin 5 → 6)

Este proyecto se actualizó a Javalin 6. Los cambios de API más relevantes fueron:

- Las rutas ahora se registran en la **fase de configuración** con
  `config.router.apiBuilder(...)` (antes `app.routes(...)` tras iniciar el servidor).
- CORS: `config.bundledPlugins.enableCors(cors -> cors.addRule(rule -> rule.anyHost()))`.
- Route overview: `config.bundledPlugins.enableRouteOverview("/rutas")`.
- Plantillas: `config.fileRenderer(new JavalinThymeleaf())` (antes `JavalinRenderer.register(...)`).
- El plugin Shadow migró al fork mantenido `com.gradleup.shadow`.
