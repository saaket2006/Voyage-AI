# Stage 1: Build Application
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /build

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Lightweight Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r voyage && useradd -r -g voyage voyage
COPY --from=builder /build/target/voyage-ai-backend-0.0.1-SNAPSHOT.jar app.jar

USER voyage
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
