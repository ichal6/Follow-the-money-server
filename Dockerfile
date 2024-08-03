FROM maven:3.9.8-eclipse-temurin-21-alpine AS maven_build

# copy the pom and src code to the container
COPY ./ ./

# package our application code
RUN mvn clean package

FROM eclipse-temurin:21-jdk-alpine

COPY --from=maven_build /target/follow-the-money-server-0.0.1-SNAPSHOT.jar /ftm.jar

# set the startup command to execute the jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/ftm.jar"]
