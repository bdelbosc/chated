chat ed - Chatting while editing
=================


## Intro

Part of the Nuxeo Sprint 2012

Using [Vert.x](http://vertx.io/) as a websocket server to chat and
notify Nuxeo users that are concurrently modifying a document.

## Overview

There is one [Vertx instance](http://vertx.io/manual.html#vertx-instances) with 3
  [verticles](http://vertx.io/manual.html#verticle):
  
  1. NxIn: Listening for http request on port 8280 comming from Nuxeo
     request are JSon encoded and contains Nuxeo event. This verticle
     send the Nuxeo event into the vertx event bus.
    
  2. NxOut: Propagate specific message from the vertx event bus to Nuxeo
     using a Rest API
    
  3. ChatEd: Chat for editors listening on port 8180, handling
     websocket Cx with browsers


## Sequences

### Authentication
  
  1.  Nuxeo return an edit page with js to autosubscibe to a channel
  dedicated to the document (/chated/$docid). It also send a random
  auth token associated with the userid and the address of the vertx
  chatEd app.
    
  2. The Browser connects to the vertx ChatEd passing the token
  
  3. ChatEd set the connection in an "anonymous" state, it sends a
  message to NxOut to validate the token.
    
  4. NxOut send an HTTP request to Nuxeo, if the response is ok it
  receive the userid corresponding to the token. NxOut retransmit
  to ChatEd the good news using the event bus.
    
  5. ChatEd change the state of the connection to be authentified.


### Retransmiting Nuxeo events
  
  1. Nuxeo using a listener send document event to the NxIn vertx server
  
  2. The NxIn server retransmit message on document channels
  
  3. The browser receive notification and display it


### Chatting
  
  1. ChatEd retransmit message to dedicated document channel working
  like a chat


## Project status

 Step 1: Skip the auth, no rest api needed on Nuxeo
 Step 2: Impl auth

## Requirement

- OpenJDK 7 (Vert.x is not working with JDK 6)
- Install [Vert.x 1.2.3-final](http://vertx.io/install.html), you
  should have `vertx` available on command line.

## Build and package

Produce a tgz apps:

    mvn package

## Run Vert.x instance

    cd target
    tar xzvf chated-1.0.0-SNAPSHOT-mod.tar.gz
    cd chated#1.0.0-SNAPSHOT/
    vertx run org.nuxeo.ecm.vertx.mod.ChatEd -conf chated.conf  -cp lib/chated-1.0.0-SNAPSHOT.jar

or to rebuild and run simply:

    ./run.sh


## Testing

### Without Nuxeo

Go to [http://localhost:8180/test](http://localhost:8180/test)

Then:

1. Open a connection.
2. Subscribe to /chated/1234 (1234 is a fake docid)
3. Simulate a Nuxeo event transmission:

    curl -i -H "Accept: application/json" -X POST -d '{"docid":"1234","event":"DocumentSave"}" http://localhost:8280/

You should see a notification on the browser.

## References

- [Vert.X](http://vertx.io/) 
- [WebSocket 101](http://lucumr.pocoo.org/2012/9/24/websockets-101/)

## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep) and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management), [digital asset management](http://www.nuxeo.com/en/products/dam) and [case management](http://www.nuxeo.com/en/products/case-management). Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

More information on: <http://www.nuxeo.com/>
