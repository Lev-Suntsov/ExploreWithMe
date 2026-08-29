  ewm-service:
    build: ./ewm-service
    container_name: ewm-service
    depends_on:
      ewm-db:
        condition: service_healthy
      stats-server:
        condition: service_started
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://ewm-db:5432/ewm
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: diplom
      STATS_SERVER_URL: http://stats-server:9090
