FROM openjdk:8-jdk-alpine
MAINTAINER https://github.com/8451
VOLUME /tmp
COPY target/code-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
