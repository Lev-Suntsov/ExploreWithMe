FROM eclipse-temurin:11-jre
WORKDIR /app
COPY target/main-service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
COPY postman /app/postman
COPY tests /app/tests
ENTRYPOINT ["java", "-jar", "app.jar"]
