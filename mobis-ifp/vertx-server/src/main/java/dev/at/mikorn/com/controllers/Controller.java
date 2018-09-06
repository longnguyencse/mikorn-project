package dev.at.mikorn.com.controllers;

import dev.at.mikorn.com.services.KafkaService;
import dev.at.mikorn.com.services.KafkaServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Author Mikorn vietnam
 * Created on 04-Sep-18.
 */

public abstract class Controller extends AbstractVerticle {

    private Logger logger = LogManager.getLogger(Controller.class);
    /**
     * https://vertx.io/blog/some-rest-with-vert-x/
     * @throws Exception
     */
    @Override
    public void start(Future<Void> fut) throws Exception {
        Router router = Router.router(vertx);
        enableCORS(router);
        KafkaService service = new KafkaServiceImpl();
        router.route().handler(BodyHandler.create());

        router.route(HttpMethod.POST, "/post-gt-data").handler(service::handleKafkaClientApi);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080, result -> {
            if (result.succeeded()) {
                fut.complete();
            } else {
                fut.fail(result.cause());
            }
        });
        logger.info(System.getProperty("user.dir"));
    }

    private void enableCORS(Router router) {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");
        allowedHeaders.add("X-PINGARUNER");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        /*
         * these methods aren't necessary for this sample,
         * but you may need them for your projects
         */
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);
        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
    }
}
