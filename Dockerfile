# syntax=docker/dockerfile:1

# --- build: compila o JAR com Maven (sem depender do Maven/Java locais) ---
FROM maven:3.9.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package && cp target/*.jar /app/app.jar

# --- runtime: JRE leve para rodar o JAR ---
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
COPY --from=build /app/app.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
