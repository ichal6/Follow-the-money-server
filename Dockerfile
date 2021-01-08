FROM openjdk:14-alpine
MAINTAINER mlkb.com
VOLUME /tmp
EXPOSE 8080
ADD target/follow-the-money-server-0.0.1-SNAPSHOT.jar follow-the-money-server-0.0.1-SNAPSHOT.jar
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/follow-the-money-server-0.0.1-SNAPSHOT.jar"]
