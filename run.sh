#!/bin/bash
mvn -o package || exit 1
cd target
tar xzvf chated-1.0.0-SNAPSHOT-mod.tar.gz
cd chated#1.0.0-SNAPSHOT/
vertx run org.nuxeo.ecm.vertx.mod.ChatEd -conf chated.conf  -cp lib/chated-1.0.0-SNAPSHOT.jar 

