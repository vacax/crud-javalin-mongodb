# =========================================================================
# Etapa 1: compilación del proyecto (fat JAR con Shadow)
# =========================================================================
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copiamos primero el wrapper y los scripts de Gradle para aprovechar la caché
# de capas de Docker: si no cambian las dependencias, no se vuelven a descargar.
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Descarga de dependencias (capa cacheable).
RUN ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

# Copiamos el código fuente y generamos el JAR.
COPY src ./src
RUN ./gradlew --no-daemon clean shadowJar

# =========================================================================
# Etapa 2: imagen de ejecución (solo el JRE, mucho más liviana)
# =========================================================================
FROM eclipse-temurin:25-jre

WORKDIR /app

# El shadowJar se genera como crud-dns.jar (ver build.gradle).
COPY --from=build /app/build/libs/crud-dns.jar app.jar

# Puerto por defecto de la aplicación.
EXPOSE 7000

ENTRYPOINT ["java", "-jar", "app.jar"]
