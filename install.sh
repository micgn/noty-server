#!/bin/bash

export VOLS=/home/mgnatz/volumes
git pull && docker kill noty-server && mvn install -DskipTests=true && docker build -t micgn/noty-server:1.4 . && ~/git/orchester/start-noty-server.sh
