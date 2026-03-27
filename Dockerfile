FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY auth-api/pom.xml  auth-api/pom.xml
COPY data-api/pom.xml  data-api/pom.xml

RUN ./mvnw -B dependency:go-offline

COPY auth-api/src  auth-api/src
COPY data-api/src  data-api/src
RUN ./mvnw -B clean package -DskipTests

# ---- auth-api runtime ----
FROM eclipse-temurin:21-jre-alpine AS auth-runtime
WORKDIR /app
COPY --from=builder /app/auth-api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# ---- data-api runtime ----
FROM eclipse-temurin:21-jre-alpine AS data-runtime
WORKDIR /app
COPY --from=builder /app/data-api/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]