package com.freeletics.bowlinggg.verticle.server;


import com.freeletics.bowlinggg.config.Addresses;
import com.freeletics.bowlinggg.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

/**
 * This is the main verticle, here an implementation of a web server will be done.
 *
 * @author Moath
 */
public class WebServer extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(WebServer.class);

    @Override
    public void start(Future<Void> future) {
        Router router = Router.router(vertx);

        router.route(HttpMethod.GET, Addresses.HEALTH_CHECK).handler(context -> context.response().setStatusCode(HttpStatus.SC_OK).end());

        router.route(HttpMethod.POST, Addresses.GAMES_ENDPOINT).handler(requestHandler());
        router.route(HttpMethod.PUT, Addresses.GAMES_ENDPOINT).handler(requestHandler());
        router.route(HttpMethod.GET, Addresses.GAMES_ENDPOINT).handler(requestHandler());
        router.route(HttpMethod.DELETE, Addresses.GAMES_ENDPOINT).handler(requestHandler());

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(vertx.getOrCreateContext().config().getInteger(Config.HTTP_PORT), result -> {
                    if (result.succeeded()) {
                        LOG.info(getClass().getSimpleName() + " was deployed successfully.");
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

    private Handler<RoutingContext> requestHandler() {
        return routingContext -> routingContext.response().end();
    }

}
