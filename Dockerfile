FROM maven:3.9.8-eclipse-temurin-21-alpine AS maven_build

# copy the pom and src code to the container
COPY ./ ./

# Install binutils, required by jlink
RUN apk update &&  \
    apk add binutils

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /optimized-jdk-21

# Second stage, Use the custom JRE and build the app image
FROM alpine:latest
ENV JAVA_HOME=/opt/jdk/jdk-21
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=maven_build /optimized-jdk-21 $JAVA_HOME

# Create the application directory
RUN mkdir /app

# Copy jar to destination directory
COPY target/*.jar /app/ftm.jar

WORKDIR /app

# set the startup command to execute the jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/ftm.jar"]
