package edu.pucmm.pw.controladores;


import edu.pucmm.pw.entidades.Estudiante;
import edu.pucmm.pw.servicios.EstudianteServices;
import edu.pucmm.pw.util.BaseControlador;
import edu.pucmm.pw.util.NoExisteEstudianteException;
import io.javalin.config.JavalinConfig;


import static io.javalin.apibuilder.ApiBuilder.*;

public class ApiControlador extends BaseControlador {

    private EstudianteServices estudianteServices = EstudianteServices.getInstancia();

    public ApiControlador(JavalinConfig config) {
        super(config);
    }

    @Override
    public void aplicarRutas() {
        config.router.apiBuilder(() -> {
            path("/api", () -> {
                /**
                 * Ejemplo de una API REST, implementando el CRUD
                 * ir a
                 */
                path("/estudiante", () -> {
                    after(ctx -> {
                        ctx.header("Content-Type", "application/json");
                    });

                    get("/", ctx -> {
                        ctx.json(estudianteServices.listarEstudiante());
                    });

                    get("/{matricula}", ctx -> {
                        ctx.json(estudianteServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).get()));
                    });

                    post("/", ctx -> {
                        //parseando la informacion del POJO debe venir en formato json.
                        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
                        //creando.
                        ctx.json(estudianteServices.crearEstudiante(tmp));
                    });

                    put("/", ctx -> {
                        //parseando la informacion del POJO.
                        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
                        //creando.
                        ctx.json(estudianteServices.actualizarEstudiante(tmp));

                    });

                    delete("/{matricula}", ctx -> {
                        //creando.
                        ctx.json(estudianteServices.eliminandoEstudiante(ctx.pathParamAsClass("matricula", Integer.class).get()));
                    });
                });
            });
        });

        //El manejo de excepciones se registra sobre el router en la configuración.
        config.router.mount(router -> {
            router.exception(NoExisteEstudianteException.class, (exception, ctx) -> {
                ctx.status(404);
                ctx.json("" + exception.getLocalizedMessage());
            });
        });
    }
}
