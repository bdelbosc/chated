/*
 * Copyright (c) 2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Benoit Delbosc
 */

package org.nuxeo.ecm.vertx.mod;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.deploy.Verticle;

/**
 * WebSocket server to handle browser communication.
 *
 */
public class ChatEd extends Verticle {

    public static final String CHANNEL_NXIN = "chated.nxin";

    public static final String EVENT_BUS_ROOT = "/chated";

    public static final String DOCID = "docid";

    public static final String EVENTNAME = "eventName";

    public static final String EVENTDATE = "eventDate";

    public static final String USERID = "userId";

    public void start() {
        final Logger logger = container.getLogger();
        JsonObject config = container.getConfig();
        logger.info("ChatEd config is " + config);

        HttpServer httpServer = vertx.createHttpServer();

        // Serve the static resources.
        httpServer.requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                if (req.path.equals("/test"))
                    req.response.sendFile("index.html");
                if (req.path.endsWith("vertxbus.js"))
                    req.response.sendFile("vertxbus.js");
            }
        });

        // Subscribe to nuxeo message
        final EventBus eb = vertx.eventBus();
        Handler<Message<JsonObject>> myHandler = new Handler<Message<JsonObject>>() {
            public void handle(Message<JsonObject> message) {
                logger.info("ChatEd Received a nuxeo message "
                        + message.body.toString());
                String docid = null;
                try {
                    docid = message.body.getString(DOCID);
                } catch (ClassCastException e) {
                    logger.error("Receive invalid docid");
                    return;
                }
                if (docid == null || docid.isEmpty()) {
                    logger.error("Missing docid");
                    return;
                }
                // Translate into a text message
                String eventDate = message.body.getString(EVENTDATE, Long.valueOf(System.currentTimeMillis()).toString());
                long timeStamp = Long.valueOf(eventDate);
                eventDate = new SimpleDateFormat("HH:mm:ss").format(new Date(timeStamp));
                String eventName = message.body.getString(EVENTNAME, "UnknownEvent");
                String userId = message.body.getString(USERID, "UnknownUser");
                String text = String.format("%s: %s by %s", eventDate, eventName, userId);
                message.body.putString("text", text);
                logger.info("ChatEd retransmit message to " + EVENT_BUS_ROOT
                        + "/" + docid);
                eb.publish(EVENT_BUS_ROOT + "/" + docid, message.body);
            }
        };
        eb.registerHandler(CHANNEL_NXIN, myHandler);

        // deploy NxIn server
        container.deployVerticle("org.nuxeo.ecm.vertx.mod.NxIn", config);

        JsonArray permitted = new JsonArray();
        permitted.add(new JsonObject()); // Let everything through
        SockJSServer sockJSServer = vertx.createSockJSServer(httpServer);
        sockJSServer.bridge(
                new JsonObject().putString("prefix", EVENT_BUS_ROOT),
                permitted, permitted);

        httpServer.websocketHandler(new Handler<ServerWebSocket>() {
            public void handle(ServerWebSocket ws) {
                // A WebSocket has connected!
                logger.info("A client has connected!" + ws.path + " " + ws.toString());

            }
        });

        httpServer.listen((Integer) config.getNumber("chated_port"));
    }

}
