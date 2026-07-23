package edu.pucmm.pw.util;

import io.javalin.config.JavalinConfig;

/**
 * Clase base para los controladores.
 *
 * A partir de Javalin 6 las rutas se registran durante la fase de configuración
 * ({@code Javalin.create(config -> ...)}) y ya no directamente sobre la instancia
 * del servidor. Por eso los controladores reciben el {@link JavalinConfig}.
 */
public abstract class BaseControlador {

    protected JavalinConfig config;

    public BaseControlador(JavalinConfig config) {
        this.config = config;
    }

    abstract public void aplicarRutas();
}
