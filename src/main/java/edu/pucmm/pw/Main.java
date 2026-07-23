package edu.pucmm.pw;

import edu.pucmm.pw.controladores.ApiControlador;
import edu.pucmm.pw.controladores.CrudTradicionalControlador;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    public static void main(String[] args) {
        System.out.println("CRUD Javalin MongoDB");

        //Creando la instancia del servidor y configurando.
        Javalin app = Javalin.create(config -> {
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.aliasCheck = null;
            });

            //Registrando el motor de plantillas Thymeleaf (antes JavalinRenderer.register).
            config.fileRenderer(new JavalinThymeleaf());

            //Habilitando el CORS. Ver: https://javalin.io/plugins/cors para más opciones.
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.anyHost();
                });
            });

            //habilitando el plugin con el listado de las rutas definidas.
            config.bundledPlugins.enableRouteOverview("/rutas");

            //incluyendo los controladores (las rutas se registran en la configuración).
            new ApiControlador(config).aplicarRutas();
            new CrudTradicionalControlador(config).aplicarRutas();
        });

        //Iniciando la aplicación
        app.start(getPuertoDinamico());
    }

    /**
     * Obtiene el puerto de la variable de entorno {@code PORT} o retorna 7000 por defecto.
     *
     * @return el puerto en el que se levantará el servidor.
     */
    static int getPuertoDinamico() {
        String puerto = System.getenv("PORT");
        if (puerto != null) {
            return Integer.parseInt(puerto);
        }
        return 7000; //Retorna el puerto por defecto.
    }
}
