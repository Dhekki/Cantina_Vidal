FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM bellsoft/liberica-runtime-container:jre-21-slim-musl

WORKDIR /app

USER root

RUN addgroup -S spring && adduser -S spring -G spring

RUN mkdir -p /app/uploads && chown -R spring:spring /app/uploads

USER spring:spring

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
