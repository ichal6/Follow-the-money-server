# Define build-time variables for Maven and Java versions
ARG MAVEN_VERSION=3.9.8
ARG JAVA_VERSION=21
ARG MAVEN_IMAGE_VERSION=$MAVEN_VERSION-eclipse-temurin-$JAVA_VERSION

FROM maven:${MAVEN_IMAGE_VERSION}-alpine AS maven_build

# Use the build-time variables
ARG MAVEN_VERSION
ARG JAVA_VERSION

RUN mkdir /opt/app
COPY . /opt/app

WORKDIR /opt/app

ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

# Install binutils, required by jlink
RUN apk update && \
    apk add --no-cache tar binutils

RUN wget https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
  mv apache-maven-$MAVEN_VERSION /usr/lib/mvn

RUN mvn package -DskipTests
RUN jar xvf target/follow-the-money-server-0.0.1-SNAPSHOT.jar
RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release ${JAVA_VERSION}  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    target/follow-the-money-server-0.0.1-SNAPSHOT.jar > modules.txt

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules $(cat modules.txt) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /optimized-jdk-${JAVA_VERSION}

# Second stage, Use the custom JRE and build the app image
FROM alpine:latest

# Use the build-time variable for Java version
ARG JAVA_VERSION

ENV JAVA_HOME=/opt/jdk/jdk-${JAVA_VERSION}
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=maven_build /optimized-jdk-${JAVA_VERSION} $JAVA_HOME

# Add app user
ARG APPLICATION_USER=spring

# Create a user to run the application, don't run as root
RUN addgroup --system $APPLICATION_USER &&  adduser --system $APPLICATION_USER --ingroup $APPLICATION_USER

# Create the application directory
RUN mkdir /app && chown -R $APPLICATION_USER /app

# Copy jar to destination directory
COPY --chown=$APPLICATION_USER:$APPLICATION_USER target/*.jar /app/ftm.jar

WORKDIR /app

USER $APPLICATION_USER

# set the startup command to execute the jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/ftm.jar"]
