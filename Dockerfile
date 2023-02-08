FROM maven:3.8-openjdk-18-slim AS MAVEN_BUILD

# copy the pom and src code to the container
COPY ./ ./

# package our application code
RUN mvn clean package

FROM openjdk:18.0.2.1-slim

COPY --from=MAVEN_BUILD /target/follow-the-money-server-0.0.1-SNAPSHOT.jar /ftm.jar

VOLUME /tmp
EXPOSE 8080
MAINTAINER mlkb.com

# set the startup command to execute the jar
CMD ["java","-Dspring.profiles.active=prod", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/ftm.jar"]
