package org.nuxeo.ecm.vertx.mod;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.deploy.Verticle;

public class NxIn extends Verticle {

    public void start() {
        final Logger logger = container.getLogger();
        JsonObject config = container.getConfig();
        HttpServer server = vertx.createHttpServer();
        // EventBus eb = vertx.eventBus();
        System.out.println("Config is " + config);
        server.requestHandler(new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest request) {
                request.bodyHandler(new Handler<Buffer>() {
                    public void handle(Buffer body) {
                        // The entire body has now been received
                        logger.info("The total body received was "
                                + body.length() + " bytes");
                        request.response.statusCode = 200;
                        request.response.statusMessage = "Cool thanks";
                        request.response.end();
                    }
                });

            }
        });
        server.listen((Integer) config.getNumber("nxin_port"));
    }

    // todo: receive json and resubmit to event bus

}
