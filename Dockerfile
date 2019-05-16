FROM ubuntu:bionic

RUN apt-get update
RUN apt-get install -y openjdk-8-jdk

COPY docker/run-app.sh /opt/noty-server/run-app.sh
RUN chmod u+x /opt/noty-server/run-app.sh

COPY target/noty-server.jar /opt/noty-server/noty-server.jar

ENV DATABASE_PATH /opt/noty-server/hsqldb/noty-server/noty-server

# dummy values:
ENV BASICAUTH_PASSWORD 12345
ENV HOST localhost:8080

VOLUME /opt/noty-server/hsqldb

CMD /opt/noty-server/run-app.sh

EXPOSE 8080
