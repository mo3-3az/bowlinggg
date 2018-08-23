package com.freeletics.bowlinggg.verticle.server;


import com.freeletics.bowlinggg.config.Config;
import com.freeletics.bowlinggg.verticle.Addresses;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * This is the main verticle, here an implementation of a web server will be done.
 *
 * @author Moath
 */
public class WebServer extends AbstractVerticle {

    @Override
    public void start(Future<Void> future) {
        Router router = Router.router(vertx);

        final JsonObject config = vertx.getOrCreateContext().config();

        router.route(HttpMethod.POST, Addresses.GAMES_ENDPOINT).handler(postRequestHandler());

        router.route(HttpMethod.PUT, Addresses.GAMES_ENDPOINT).handler(putRequestHandler());

        router.route(HttpMethod.GET, Addresses.GAMES_ENDPOINT).handler(getRequestHandler());

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config.getInteger(Config.HTTP_PORT), result -> {
                    if (result.failed()) {
                        future.fail(result.cause());
                    } else {
                        future.complete();
                    }
                });
    }

    private Handler<RoutingContext> postRequestHandler() {
        return routingContext -> routingContext.response().end();
    }

    private Handler<RoutingContext> putRequestHandler() {
        return routingContext -> routingContext.response().end();
    }

    private Handler<RoutingContext> getRequestHandler() {
        return routingContext -> routingContext.response().end();
    }

}
