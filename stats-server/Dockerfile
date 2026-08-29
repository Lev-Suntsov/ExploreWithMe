FROM eclipse-temurin:11-jre

WORKDIR /app

COPY target/stats-server-1.0-SNAPSHOT.jar app.jar

EXPOSE 9090

ENTRYPOINT ["./wait-for-it.sh", "stats-server:9090", "java", "-jar", "app.jar"]