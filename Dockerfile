# Etapa 1 — compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Descargar dependencias primero (esto se guarda en caché si pom.xml no cambia)
RUN mvn dependency:go-offline -B

COPY src ./src
# Limitar la memoria de Maven para evitar que el servidor colapse (swap)
ENV MAVEN_OPTS="-Xmx512m"
RUN mvn clean package -DskipTests

# Etapa 2 — imagen final liviana
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Duser.timezone=America/Lima", "-jar", "app.jar"]