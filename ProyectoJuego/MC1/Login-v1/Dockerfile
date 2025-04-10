# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copiar el pom.xml y src desde el contexto de construcción
COPY ./pom.xml ../pom.xml
COPY ./src ./src
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 7001
ENTRYPOINT ["java", "-jar", "app.jar"]
