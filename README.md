Chat ed - Chatting while editing
=================

Part of the Nuxeo Sprint 2012

## Build

Produce a tgz apps:

    mvn package

## Test

	cd target
	tar xzvf chated-1.0.0-SNAPSHOT-mod.tar.gz
    cd chated#1.0.0-SNAPSHOT/
    vertx run org.nuxeo.ecm.vertx.mod.ChatEd -conf chated.conf  -cp lib/chated-1.0.0-SNAPSHOT.jar 
	
	

## About Nuxeo

Nuxeo provides a modular, extensible Java-based [open source software platform for enterprise content management](http://www.nuxeo.com/en/products/ep) and packaged applications for [document management](http://www.nuxeo.com/en/products/document-management), [digital asset management](http://www.nuxeo.com/en/products/dam) and [case management](http://www.nuxeo.com/en/products/case-management). Designed by developers for developers, the Nuxeo platform offers a modern architecture, a powerful plug-in model and extensive packaging capabilities for building content applications.

More information on: <http://www.nuxeo.com/>
