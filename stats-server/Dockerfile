FROM eclipse-temurin:11-jre

WORKDIR /app

COPY target/stats-server-1.0-SNAPSHOT.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]