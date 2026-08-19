FROM eclipse-temurin:11-jre
WORKDIR /app
COPY target/ewm-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
