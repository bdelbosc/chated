chat ed - Chating while editing
=================


## Intro

Part of the Nuxeo Sprint 2012

Using Vert.x as a websocket server to chat and notify editors when
there are more than one editor on the same document.



## Build

Produce a tgz apps:

    mvn package

## Start the Vert.x instance

	cd target
	tar xzvf chated-1.0.0-SNAPSHOT-mod.tar.gz
    cd chated#1.0.0-SNAPSHOT/
    vertx run org.nuxeo.ecm.vertx.mod.ChatEd -conf chated.conf  -cp lib/chated-1.0.0-SNAPSHOT.jar 
	
or 

	./run.sh
	
	
## Testing

Go to [http://localhost:8180/test](http://localhost:8180/test)

Then Open a connection.

Simulate a Nuxeo event transmission:

    curl -i -H "Accept: application/json" -X POST -d "{doc: 123412341234, event: saved}" http://localhost:8280/

You should see a notification on the browser.


## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep) and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management), [digital asset management](http://www.nuxeo.com/en/products/dam) and [case management](http://www.nuxeo.com/en/products/case-management). Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

More information on: <http://www.nuxeo.com/>
