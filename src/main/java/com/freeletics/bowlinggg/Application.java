package com.freeletics.bowlinggg;

import com.freeletics.bowlinggg.verticle.Deployer;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import org.apache.log4j.Logger;

/**
 * @author Moath
 */
public class Application {
    private static final Logger LOG = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        ConfigRetriever retriever = ConfigRetriever.create(vertx);
        retriever.getConfig(configHandler -> {
            if (configHandler.failed()) {
                LOG.fatal("Failed to load configurations!", configHandler.cause());
                return;
            }

            vertx.deployVerticle(new Deployer(configHandler.result()), deployment -> {
                if (deployment.failed()) {
                    LOG.fatal(Deployer.class.getSimpleName() + " wasn't deployed successfully.");
                }
            });
        });
    }

}
