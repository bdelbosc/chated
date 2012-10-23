package org.nuxeo.ecm.vertx.mod;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.core.streams.Pump;
import org.vertx.java.deploy.Verticle;

/**
 * WebSocket server to handle browser communication.
 *
 */
public class ChatEd extends Verticle {
    public void start() {
        Logger logger = container.getLogger();
        JsonObject config = container.getConfig();
        System.out.println("Config is " + config);

        HttpServer httpServer = vertx.createHttpServer();
        SockJSServer sockJSServer = vertx.createSockJSServer(httpServer);
        config = config.putString("prefix", "/chated");

        // deploy NxIn server
        container.deployVerticle("org.nuxeo.ecm.vertx.mod.NxIn", config);

        sockJSServer.installApp(config, new Handler<SockJSSocket>() {
            public void handle(SockJSSocket sock) {
                Pump.createPump(sock, sock).start();
            }
        });

        httpServer.listen((Integer) config.getNumber("chated_port"));
    }

}
