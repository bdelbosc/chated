chat ed - Chatting while editing
=================


## Intro

Part of the Nuxeo Sprint 2012

Using Vert.x as a websocket server to chat and notify Nuxeo users that
are concurrently modifying a document.

## Sequence

  There is one Vertx instance with 3 verticles:
  - NxIn: receive http request from Nuxeo (json message) en send it to
    the vertx event bus
  - NxOut: propagate specific message from the event bus to Nuxeo
    using a Rest API
  - ChatEd: chat for editors, handling websocket Cx with browsers

  Auth:
  - Nuxeo return an edit page with js to autosubscibe to a channel
    dedicated to the document. It also send a random auth token
    associated with the userid and the address of the vertx chat-edit
    app.
  - The Browser connects to the vertx chat-edit passing the token
  - The vertx chat-edit set the connection in a "anonymous" state, it
    sends a message to nx-out to validate the token.
  - The nx-out receive the message and sent an HTTP rest request to Nuxeo
    if the response is ok it send a positive reply with the userid,
    the nx-out send the good news to chat-edit
  - The chat-edit set the connection as logged as userid

  Retransmiting events:
  - Nuxeo using a listener send document event to the nx-in vertx server
  - The nx-in server retransmit message on document channels
  - The browser receive notification and display it

  Inter editor chat:
  - User can interact using the chat edit using their dedicated channel


## Plan

### Step 1

- Skip the auth, no rest api needed on Nuxeo


### Step 2

- Impl auth

## Requierment

- OpenJDK 7 (Vert.x is not working with JDK 6)
- Install Vert.x 1.2.3-final, having vertx available on command line

## Build and package

Produce a tgz apps:

    mvn package

## Run Vert.x instance

    cd target
    tar xzvf chated-1.0.0-SNAPSHOT-mod.tar.gz
    cd chated#1.0.0-SNAPSHOT/
    vertx run org.nuxeo.ecm.vertx.mod.ChatEd -conf chated.conf  -cp lib/chated-1.0.0-SNAPSHOT.jar

or

    ./run.sh

## Testing

Go to [http://localhost:8180/test](http://localhost:8180/test)

Then:
1/ Open a connection.
2/ Subscribe to /chated/1234 (1234 is a fake docid)
3/ Simulate a Nuxeo event transmission:

    curl -i -H "Accept: application/json" -X POST -d '{"docid":"1234","event":"DocumentSave"}" http://localhost:8280/

You should see a notification on the browser.


## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep) and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management), [digital asset management](http://www.nuxeo.com/en/products/dam) and [case management](http://www.nuxeo.com/en/products/case-management). Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

More information on: <http://www.nuxeo.com/>
