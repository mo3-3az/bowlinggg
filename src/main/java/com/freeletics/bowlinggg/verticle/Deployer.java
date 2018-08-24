package com.freeletics.bowlinggg.verticle;

import com.freeletics.bowlinggg.verticle.game.GameManager;
import com.freeletics.bowlinggg.verticle.server.WebServer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.Logger;

/**
 * @author Moath
 */
public class Deployer extends AbstractVerticle {

    private static final Logger LOG = Logger.getLogger(Deployer.class);

    private JsonObject config;

    public Deployer(JsonObject config) {
        this.config = config;
    }

    @Override
    public void start(Future<Void> startFuture) {
        DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(config);
        vertx.deployVerticle(new GameManager(), deploymentOptions, deployGameManager -> {
            if (deployGameManager.succeeded()) {
                vertx.deployVerticle(WebServer.class.getName(), deploymentOptions, deployWebServer -> {
                    if (deployWebServer.succeeded()) {
                        LOG.info(getClass().getSimpleName() + " was deployed successfully.");
                        startFuture.complete();
                    } else {
                        LOG.fatal(WebServer.class.getSimpleName() + " wasn't deployed successfully.");
                        startFuture.fail(deployWebServer.cause());
                    }
                });
            } else {
                LOG.fatal(GameManager.class.getSimpleName() + " wasn't deployed successfully.");
                startFuture.fail(deployGameManager.cause());
            }
        });
    }
}
