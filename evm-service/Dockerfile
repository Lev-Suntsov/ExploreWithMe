FROM eclipse-temurin:11-jre
WORKDIR /app

# 1. Сначала копируем сам скрипт ожидания (он лежит в корне папки evm-service)
COPY wait-for-it.sh wait-for-it.sh

# 2. Выдаем ему права на чтение и запуск внутри Linux-контейнера
RUN chmod +x wait-for-it.sh

# 3. Копируем собранный исполняемый jar-файл приложения
COPY target/main-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

