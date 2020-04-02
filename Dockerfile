FROM maven:3.6.3-ibmjava-8-alpine AS appServer

LABEL maintainer="danijelradakovic@uns.ac.rs"

WORKDIR /usr/src/logan
COPY . .
RUN ["mvn", "package", "-DskipTests"]


FROM openjdk:8-jdk-alpine
WORKDIR /app
EXPOSE 8080
ENV LOG_STORAGE /var/log/web-traffic.log
VOLUME /var/log
COPY --from=appServer /usr/src/logan/target/logan.jar ./


CMD ["java", "-jar", "logan.jar"]
