FROM gradle:9.1.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle buildFatJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/backend-all.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
