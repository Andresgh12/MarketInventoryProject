# etapa de build con Maven y JDK 17
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# copiar pom y código fuente
COPY pom.xml .
COPY src ./src

# compilar y empaquetar
RUN mvn clean package -DskipTests

# etapa de ejecución con JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# instrucción de arranque
ENTRYPOINT ["java","-jar","app.jar"]
