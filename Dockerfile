# Etapa de build
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom.xml y src/ desde la subcarpeta donde está tu código
COPY MarketInventoryProject/pom.xml .
COPY MarketInventoryProject/src ./src



RUN mvn clean package -DskipTests

# Etapa de runtime con JRE ligero
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el JAR generado en la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Arrancamos la aplicación
ENTRYPOINT ["java","-jar","app.jar"]
