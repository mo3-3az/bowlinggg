import com.freeletics.bowlinggg.verticle.game.GameManager;
import com.freeletics.bowlinggg.verticle.server.WebServer;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.DeploymentOptions;
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

            DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(configHandler.result());
            vertx.deployVerticle(GameManager.class.getName(), deploymentOptions, deployGameManager -> {
                if (deployGameManager.succeeded()) {
                    vertx.deployVerticle(WebServer.class.getName(), deploymentOptions, deployWebServer -> {
                        if (deployWebServer.succeeded()) {
                            LOG.info(WebServer.class.getSimpleName() + " was deployed successfully.");
                        } else {
                            LOG.fatal(WebServer.class.getSimpleName() + " wasn't deployed successfully.");
                        }
                    });

                    LOG.info(GameManager.class.getSimpleName() + " was deployed successfully.");
                } else {
                    LOG.fatal(GameManager.class.getSimpleName() + " wasn't deployed successfully.");
                }
            });
        });
    }

}
