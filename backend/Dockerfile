FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r voyage && useradd -r -g voyage voyage
COPY target/voyage-ai-backend-0.0.1-SNAPSHOT.jar app.jar

USER voyage
EXPOSE 8081

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

