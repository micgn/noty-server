#!/bin/bash

rm /opt/noty-server/hsqldb/noty-server/noty-server.lck

java \
-Dspring.profiles.active=cloud \
-Dlogging.level.de.mg.noty=info \
-jar /opt/noty-server/noty-server.jar
