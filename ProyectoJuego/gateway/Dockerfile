# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar ./gateway.jar
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "gateway.jar"] 